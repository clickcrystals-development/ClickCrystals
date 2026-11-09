package io.github.itzispyder.clickcrystals.scripting.runtime;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Cooperative bridge for CPU-heavy path planning or local model inference.
 *
 * Only immutable snapshots should be sent to workers.
 * Result/error callbacks always dispatch through the supplied client-thread executor.
 * Does not expose live Minecraft objects to background threads.
 */
public final class AsyncDecisionBridge implements AutoCloseable {
    private static final int MAX_PENDING = 32;
    private final Consumer<Runnable> mainThread;
    private final ThreadPoolExecutor workers;
    private final Map<Long,FutureTask<Void>> pending = new ConcurrentHashMap<>();
    private final AtomicLong generation = new AtomicLong();
    private final AtomicLong ids = new AtomicLong();
    private volatile boolean closed;

    public AsyncDecisionBridge(Consumer<Runnable> mainThread) {
        this.mainThread=Objects.requireNonNull(mainThread);
        AtomicLong index=new AtomicLong();
        this.workers=new ThreadPoolExecutor(1,2,30,TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(16), task -> {
                    Thread thread=new Thread(task,"ClickScript-compute-"+index.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                },new ThreadPoolExecutor.AbortPolicy());
    }

    public <S,R> long submit(S snapshot, Function<S,R> calculate,
                              Consumer<R> apply, Consumer<Throwable> onError) {
        Objects.requireNonNull(calculate);
        Objects.requireNonNull(apply);
        Objects.requireNonNull(onError);
        if(closed)throw new IllegalStateException("Compute bridge is closed");
        if(pending.size()>=MAX_PENDING)throw new RejectedExecutionException("Compute task limit reached");
        long id=ids.incrementAndGet();
        long epoch=generation.get();
        FutureTask<Void> future=new FutureTask<>(()->{
            try {
                R result=calculate.apply(snapshot);
                if(!closed&&epoch==generation.get())
                    mainThread.accept(()->{
                        if(!closed&&epoch==generation.get())apply.accept(result);
                    });
            } catch (Throwable error) {
                if(!closed&&epoch==generation.get())
                    mainThread.accept(()->{
                        if(!closed&&epoch==generation.get())onError.accept(error);
                    });
            } finally {
                pending.remove(id);
            }
            return null;
        });
        pending.put(id,future);
        try {workers.execute(future);}
        catch(RejectedExecutionException error) {pending.remove(id);throw error;}
        return id;
    }

    public boolean cancel(long id) {
        FutureTask<Void> future=pending.remove(id);
        return future!=null&&future.cancel(true);
    }

    /** Invalidates all old results, even those already posted to the main-thread queue. */
    public void cancelAll() {
        generation.incrementAndGet();
        for(FutureTask<Void> f:pending.values())f.cancel(true);
        pending.clear();
        workers.getQueue().clear();
    }

    public int pendingCount() {return pending.size();}

    @Override public void close() {
        closed=true;
        cancelAll();
        workers.shutdownNow();
    }
}
