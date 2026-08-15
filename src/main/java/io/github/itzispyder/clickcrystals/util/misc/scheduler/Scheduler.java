package io.github.itzispyder.clickcrystals.util.misc.scheduler;

import io.github.itzispyder.clickcrystals.Global;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

    public SchedulerResult runDelayedTask(Task task, long delayMillis) {
        activeTasks.incrementAndGet();
        AtomicReference<SchedulerResult> futureReference = new AtomicReference<>();
        futureReference.set(new SchedulerResult(
                worker.schedule(task.asRunnable(futureReference, activeTasks::decrementAndGet), delayMillis, TimeUnit.MILLISECONDS),
                activeTasks::decrementAndGet));
        return futureReference.get();
    }

    public SchedulerResult runRepeatingTask(Task task, long delayMillis, long periodMillis) {
        activeTasks.incrementAndGet();
        AtomicReference<SchedulerResult> futureReference = new AtomicReference<>();
        futureReference.set(new SchedulerResult(
                worker.scheduleAtFixedRate(task.asRunnable(futureReference), delayMillis, periodMillis, TimeUnit.MILLISECONDS),
                activeTasks::decrementAndGet));
        return futureReference.get();
    }

    public SchedulerResult runRepeatingTask(Task task, long delayMillis, long periodMillis, int iterations) {
        if (iterations == INFINITE_ITERATIONS)
            return runRepeatingTask(task, delayMillis, periodMillis);

        activeTasks.incrementAndGet();
        AtomicReference<SchedulerResult> futureReference = new AtomicReference<>();
        futureReference.set(new SchedulerResult(worker.scheduleAtFixedRate(new Runnable() {
            private int iterationCount;

            @Override
            public void run() {
                if (iterationCount++ < iterations) {
                    task.run(futureReference);
                }
                else {
                    futureReference.get().cancel();
                    activeTasks.decrementAndGet();
                }
            }
        }, delayMillis, periodMillis, TimeUnit.MILLISECONDS), activeTasks::decrementAndGet));
        return futureReference.get();
    }

    @FunctionalInterface
    public interface Task {
        void run(AtomicReference<SchedulerResult> self);

        default Runnable asRunnable(AtomicReference<SchedulerResult> self, Runnable onFinish) {
            return () -> {
                run(self);
                
                if (onFinish != null)
                    onFinish.run();
            };
        }

        default Runnable asRunnable(AtomicReference<SchedulerResult> self) {
            return asRunnable(self, null);
        }
    }
}