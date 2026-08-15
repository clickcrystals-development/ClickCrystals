package io.github.itzispyder.clickcrystals.util.misc.scheduler;

import io.github.itzispyder.clickcrystals.Global;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Scheduler implements Global {

    public static final int INFINITE_ITERATIONS = -1;

    private static final AtomicInteger activeTasks = new AtomicInteger(0);

    private final ScheduledExecutorService worker;

    public Scheduler() {
        worker = Executors.newScheduledThreadPool(2);
    }

    public void cancelAllTasks() {
        worker.shutdownNow();
    }

    public int count() {
        return activeTasks.get();
    }

    public SchedulerChain runChainTask() {
        return new SchedulerChain(this);
    }

    public ScheduledFuture<?> runDelayedTask(Task task, long delayMillis) {
        activeTasks.incrementAndGet();
        AtomicReference<ScheduledFuture<?>> futureReference = new AtomicReference<>();
        futureReference.set(worker.schedule(task.asRunnable(futureReference), delayMillis, TimeUnit.MILLISECONDS));
        return futureReference.get();
    }

    public ScheduledFuture<?> runRepeatingTask(Task task, long delayMillis, long periodMillis) {
        activeTasks.incrementAndGet();
        AtomicReference<ScheduledFuture<?>> futureReference = new AtomicReference<>();
        futureReference.set(worker.scheduleAtFixedRate(task.asRunnable(futureReference), delayMillis, periodMillis, TimeUnit.MILLISECONDS));
        return futureReference.get();
    }

    public ScheduledFuture<?> runRepeatingTask(Task task, long delayMillis, long periodMillis, int iterations) {
        activeTasks.incrementAndGet();
        if (iterations == INFINITE_ITERATIONS)
            return runRepeatingTask(task, delayMillis, periodMillis);

        AtomicReference<ScheduledFuture<?>> futureReference = new AtomicReference<>();
        futureReference.set(worker.scheduleAtFixedRate(new Runnable() {
            private int iterationCount;

            @Override
            public void run() {
                if (iterationCount++ < iterations)
                    task.run(futureReference);
                else
                    futureReference.get().cancel(true);
            }
        }, delayMillis, periodMillis, TimeUnit.MILLISECONDS));
        return futureReference.get();
    }

    @FunctionalInterface
    public interface Task {
        void run(ScheduledFuture<?> self);

        default void run(AtomicReference<ScheduledFuture<?>> self) {
            run(self.get());
        }

        default Runnable asRunnable(ScheduledFuture<?> self) {
            return () -> {
                run(self);
                activeTasks.decrementAndGet();
            };
        }

        default Runnable asRunnable(AtomicReference<ScheduledFuture<?>> self) {
            return asRunnable(self.get());
        }
    }
}