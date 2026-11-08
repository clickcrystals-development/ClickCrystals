package io.github.itzispyder.clickcrystals.scripting.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Deterministic, single-threaded tick-based task scheduler.
 * No worker threads, wall-clock timers or blocking sleeps.
 * Every callback executes on the Minecraft client thread via the caller's tick.
 */
public final class TickTaskQueue {
    public static final int MAX_TASKS = 64;
    public static final int MAX_EXECUTIONS_PER_TICK = 32;
    public static final int MAX_INTERVAL_TICKS = 72000;

    private static final Pattern NAME = Pattern.compile("[A-Za-z_][A-Za-z0-9_-]{0,63}");
    private final Map<String, Task> tasks = new LinkedHashMap<>();
    private long ticks;

    private static final class Task {
        final String key;
        final Runnable runnable;
        final int period;
        long dueTick;
        Task(String key, long dueTick, int period, Runnable runnable) {
            this.key=key; this.dueTick=dueTick; this.period=period; this.runnable=runnable;
        }
    }

    public synchronized void schedule(String name, int delayTicks, int periodTicks, Runnable action) {
        if (name == null || !NAME.matcher(name).matches())
            throw new IllegalArgumentException("Invalid task name");
        if (delayTicks < 1 || delayTicks > MAX_INTERVAL_TICKS)
            throw new IllegalArgumentException("Delay must be 1..72000 game ticks");
        if (periodTicks != 0 && (periodTicks < 1 || periodTicks > MAX_INTERVAL_TICKS))
            throw new IllegalArgumentException("Repeat interval must be 1..72000 ticks");
        if (action == null) throw new IllegalArgumentException("Task action is null");
        if (!tasks.containsKey(name) && tasks.size() >= MAX_TASKS)
            throw new IllegalStateException("Too many active script tasks (64 max)");
        tasks.put(name, new Task(name, ticks + delayTicks, periodTicks, action));
    }

    public synchronized boolean cancel(String name) {
        return tasks.remove(name) != null;
    }

    public synchronized void cancelAll() {
        tasks.clear();
    }

    public synchronized int size() {
        return tasks.size();
    }

    public synchronized long currentTick() {
        return ticks;
    }

    /**
     * Caller MUST invoke this on the game thread, once per client tick.
     * Tasks created by callbacks cannot execute until a future tick.
     */
    public synchronized void tick() {
        ticks++;
        int executed=0;
        for (Task task : new ArrayList<>(tasks.values())) {
            if (executed >= MAX_EXECUTIONS_PER_TICK) break;
            if (tasks.get(task.key) != task || task.dueTick > ticks) continue;

            if (task.period == 0) tasks.remove(task.key);
            else task.dueTick = ticks + task.period;
            executed++;
            try {
                task.runnable.run();
            } catch (RuntimeException ex) {
                tasks.remove(task.key, task);
                System.err.println("ClickScript task '" + task.key + "' stopped: " + ex);
            }
        }
    }
}
