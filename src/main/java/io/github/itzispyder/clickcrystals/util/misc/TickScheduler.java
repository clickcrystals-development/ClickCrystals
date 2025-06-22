package io.github.itzispyder.clickcrystals.util.misc;

import java.util.*;

public class TickScheduler {

    private static final Set<TickScheduler> SCHEDULER_REGISTRY = new HashSet<>();

    public static void tickAll() {
        List<TickScheduler> schedulers = new ArrayList<>(SCHEDULER_REGISTRY);
        schedulers.forEach(TickScheduler::tick);
    }


    private final Map<Long, List<Runnable>> schedule;
    private long currentTimeTicks;

    public TickScheduler() {
        this.schedule = new HashMap<>();
        SCHEDULER_REGISTRY.add(this);
    }

    public int count() {
        return schedule.size();
    }

    public void destroy() {
        SCHEDULER_REGISTRY.remove(this);
    }

    private void tick() {
        currentTimeTicks++;

        List<Runnable> tasks = schedule.get(currentTimeTicks);
        if (tasks == null)
            return;

        for (Runnable task: tasks)
            task.run();
        schedule.remove(currentTimeTicks);
    }

    public long currentTimeTicks() {
        return currentTimeTicks;
    }

    public void schedule(int ticks, Runnable task) {
        if (ticks <= 0) {
            task.run();
            return;
        }

        List<Runnable> currentScheduled = schedule.getOrDefault(currentTimeTicks + ticks, new ArrayList<>());
        currentScheduled.add(task);
        schedule.put(currentTimeTicks + ticks, currentScheduled);
    }

    public void schedule(double seconds, Runnable task) {
        if (seconds <= 0.0) {
            task.run();
            return;
        }

        int ticks = (int)(seconds * 20 + 0.5);
        schedule(ticks, task);
    }
}
