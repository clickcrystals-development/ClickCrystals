package io.github.itzispyder.clickcrystals.scheduler;

import io.github.itzispyder.clickcrystals.events.Listener;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

public final class Scheduler implements Listener {

    private static final Set<Task> tasks = new HashSet<>();

    public static void onTick() {
        try {
            tasks.forEach(Task::onTick);
        }
        catch (ConcurrentModificationException ignore) {}
    }

    public static Set<Task> getTasks() {
        return tasks;
    }

    public static <T extends Task> void runTask(T task) {
        tasks.add(task);
    }

    public static void purgeAll() {
        tasks.clear();
    }

    public static void runTaskNow(Runnable task) {
        task.run();
    }

    public static void runTaskLater(Runnable task, int tickDelay) {
        DelayedTask delayedTask = new DelayedTask(task, tickDelay);
        tasks.add(delayedTask);
    }

    public static void runTaskRepeating(Runnable task, int tickInterval) {
        RepeatingTask repeatingTask = new RepeatingTask(task, tickInterval);
        tasks.add(repeatingTask);
    }
}
