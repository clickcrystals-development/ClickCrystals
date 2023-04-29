package io.github.itzispyder.clickcrystals.scheduler;

import io.github.itzispyder.clickcrystals.events.Cancellable;

/**
 * ScheduleTask from Runnable, multithreaded with delay
 */
public class ScheduledTask implements Task, Cancellable {

    private final Runnable task;
    private boolean cancelled;

    /**
     * Create a scheduled task from runnable
     * Can be cancelled
     * Best with lambda
     * @param task runnable
     */
    public ScheduledTask(Runnable task) {
        this.task = task;
        this.cancelled = false;
    }

    /**
     * Run task once and kill thread
     * Can be cancelled
     */
    @Override
    public void runTask() {
        new Thread(task).start();
    }

    /**
     * Run task (times) amount of times with a (tickDelay) delay
     * then kill thread
     * Can be cancelled
     * @param times times
     * @param millisDelay millis delay
     */
    @Override
    public void runRepeatingTask(int times, long millisDelay) {
        new Thread(() -> {
            for (int i = 0; i < times && !cancelled; i++) {
                task.run();
                try {
                    Thread.sleep(millisDelay);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Repeats a task forever
     * Can be cancelled
     * @param millisDelay millis delay
     */
    @Override
    public void runTaskForever(long millisDelay) {
        new Thread(() -> {
            while (!cancelled) {
                task.run();
                try {
                    Thread.sleep(millisDelay);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Delays then runs a task then kills the thread
     * Can be cancelled
     * @param millisDelay millis delay
     */
    @Override
    public void runDelayedTask(long millisDelay) {
        new Thread(() -> {
            try {
                Thread.sleep(millisDelay);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            task.run();
        }).start();
    }

    /**
     * Returns the task that is being run
     * @return task
     */
    @Override
    public Runnable getTask() {
        return task;
    }


    /**
     * Is task cancelled
     * @return cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Set task cancelled
     * @param cancelled is cancelled
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
