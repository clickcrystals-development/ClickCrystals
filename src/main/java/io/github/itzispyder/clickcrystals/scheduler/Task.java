package io.github.itzispyder.clickcrystals.scheduler;

/**
 * Task scheduler
 */
public interface Task {

    /**
     * Run task
     */
    void runTask();

    /**
     * Run task repeatedly
     * @param times times
     * @param millisDelay delay in milliseconds
     */
    void runRepeatingTask(int times, long millisDelay);

    /**
     * Run task forever
     * @param millisDelay delay in milliseconds
     */
    void runTaskForever(long millisDelay);

    /**
     * Run a delayed task
     * @param millisDelay delay in milliseconds
     */
    void runDelayedTask(long millisDelay);

    /**
     * Get the running task
     * @return task
     */
    Runnable getTask();
}
