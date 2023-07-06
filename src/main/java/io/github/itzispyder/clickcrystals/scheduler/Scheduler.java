package io.github.itzispyder.clickcrystals.scheduler;

import io.github.itzispyder.clickcrystals.events.Listener;

import java.util.ArrayList;
import java.util.List;

public final class Scheduler implements Listener {

    private static final List<Task> tasks = new ArrayList<>();

    public static void onTick() {
        tasks.forEach(Task::onTick);
    }

    public static List<Task> getTasks() {
        return tasks;
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
