package io.github.itzispyder.clickcrystals.scripting.runtime;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Bounded asynchronous computation over immutable game-state snapshots.
 *
 * A request remains pending until its main-thread delivery has finished.
 * Therefore cancel(id) also suppresses completed calculations whose results
 * are already queued but have not yet been delivered to the game thread.
 *
 * All apply/error callbacks must run through the supplied mainThread executor.
 * Neither this bridge nor its worker threads access Minecraft objects.
 */
public final class AsyncDecisionBridge implements AutoCloseable {
    public static final int MAX_PENDING = 32;
    private final Consumer<Runnable> mainThread;
    private final ThreadPoolExecutor workers;
    private final Map<Long, Request> pending = new ConcurrentHashMap<>();
    private final AtomicLong generation = new AtomicLong();
    private final AtomicLong ids = new AtomicLong();
    private volatile boolean closed;

    private static final class Request {
        volatile boolean cancelled;
        FutureTask<Void> future;
    }

    public AsyncDecisionBridge(Consumer<Runnable> mainThread) {
        this.mainThread = Objects.requireNonNull(mainThread, "mainThread");
        AtomicLong threadIndex = new AtomicLong();
        workers = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(16), task -> {
            Thread thread = new Thread(task, "ClickScript-compute-" + threadIndex.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        }, new ThreadPoolExecutor.AbortPolicy());
    }

    private boolean current(long id, long epoch, Request request) {
        return !closed && !request.cancelled && generation.get() == epoch
                && pending.get(id) == request;
    }

    private void deliver(long id, long epoch, Request request, Runnable callback) {
        if (!current(id, epoch, request)) {
            pending.remove(id, request);
            return;
        }
        try {
            mainThread.accept(() -> {
                try {
                    if (current(id, epoch, request)) callback.run();
                } finally {
                    pending.remove(id, request);
                }
            });
        } catch (RuntimeException error) {
            pending.remove(id, request);
            throw error;
        }
    }

    public synchronized <S, R> long submit(S snapshot, Function<S, R> calculate,
                                            Consumer<R> apply, Consumer<Throwable> onError) {
        Objects.requireNonNull(calculate, "calculate");
        Objects.requireNonNull(apply, "apply");
        Objects.requireNonNull(onError, "onError");
        if (closed) throw new IllegalStateException("Compute bridge is closed");
        if (pending.size() >= MAX_PENDING)
            throw new RejectedExecutionException("Compute task limit reached");

        long id = ids.incrementAndGet();
        long epoch = generation.get();
        Request request = new Request();
        request.future = new FutureTask<>(() -> {
            try {
                R result = calculate.apply(snapshot);
                deliver(id, epoch, request, () -> apply.accept(result));
            } catch (Throwable error) {
                // A failed worker calculation is reported on the main thread.
                // If dispatch itself failed, the second attempt is also bounded
                // by deliver() and cannot resurrect cancelled work.
                try {
                    deliver(id, epoch, request, () -> onError.accept(error));
                } catch (RuntimeException dispatchError) {
                    pending.remove(id, request);
                }
            }
            return null;
        });
        pending.put(id, request);
        try {
            workers.execute(request.future);
        } catch (RejectedExecutionException error) {
            pending.remove(id, request);
            throw error;
        }
        return id;
    }

    /** Returns true even if computation finished, provided delivery was still pending. */
    public synchronized boolean cancel(long id) {
        Request request = pending.remove(id);
        if (request == null) return false;
        request.cancelled = true;
        request.future.cancel(true);
        return true;
    }

    /** Invalidates old callbacks, including those already queued on the game thread. */
    public synchronized void cancelAll() {
        generation.incrementAndGet();
        for (Request request : List.copyOf(pending.values())) {
            request.cancelled = true;
            request.future.cancel(true);
        }
        pending.clear();
        workers.getQueue().clear();
    }

    public int pendingCount() {
        return pending.size();
    }

    @Override
    public synchronized void close() {
        if (closed) return;
        closed = true;
        cancelAll();
        workers.shutdownNow();
    }
}
