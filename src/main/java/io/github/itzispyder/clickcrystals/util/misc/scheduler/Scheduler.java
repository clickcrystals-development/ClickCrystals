package io.github.itzispyder.clickcrystals.util.misc.scheduler;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.exceptions.ScriptException;
import net.minecraft.client.Minecraft;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Scheduler implements Global {

    public static final int INFINITE_ITERATIONS = -1;

    private static final Thread MAIN_THREAD = Thread.currentThread();

    private ScheduledExecutorService worker;

    public Scheduler() {
        worker = Executors.newScheduledThreadPool(2);
    }

    public void cancelAllTasks() {
        worker.shutdownNow();
        worker = Executors.newScheduledThreadPool(2);
    }

    public int count() {
        return ((ScheduledThreadPoolExecutor) worker).getQueue().size();
    }

    public SchedulerChain runChainTask() {
        return new SchedulerChain(this);
    }

    public SchedulerResult runDelayedTask(Task task, long delayMillis) {
        AtomicReference<SchedulerResult> futureReference = new AtomicReference<>();
        futureReference.set(new SchedulerResult(worker.schedule(task.asRunnable(futureReference), delayMillis, TimeUnit.MILLISECONDS)));
        return futureReference.get();
    }

    public SchedulerResult runRepeatingTask(Task task, long delayMillis, long periodMillis) {
        AtomicReference<SchedulerResult> futureReference = new AtomicReference<>();
        futureReference.set(new SchedulerResult(worker.scheduleAtFixedRate(task.asRunnable(futureReference), delayMillis, periodMillis, TimeUnit.MILLISECONDS)));
        return futureReference.get();
    }

    public SchedulerResult runRepeatingTask(Task task, long delayMillis, long periodMillis, int iterations) {
        if (iterations == INFINITE_ITERATIONS)
            return runRepeatingTask(task, delayMillis, periodMillis);

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
                }
            }
        }, delayMillis, periodMillis, TimeUnit.MILLISECONDS)));
        return futureReference.get();
    }

    public static void throwOnMainThread(Throwable throwable) {
        Minecraft.getInstance().execute(() -> {
            if (throwable instanceof RuntimeException e)
                throw e;
            throw new RuntimeException(throwable);
        });
    }

    @FunctionalInterface
    public interface Task {
        void run(AtomicReference<SchedulerResult> self);

        default Runnable asRunnable(AtomicReference<SchedulerResult> self) {
            return () -> {
                try {
                    run(self);
                }
                catch (ScriptException e) {
                    e.getExecutor().printErrorDetails(e, e.getLine());
                }
                catch (Exception e) {
                    throwOnMainThread(e);
                }
            };
        }
    }
}