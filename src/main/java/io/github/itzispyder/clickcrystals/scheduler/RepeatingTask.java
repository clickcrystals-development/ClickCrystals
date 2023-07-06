package io.github.itzispyder.clickcrystals.scheduler;

import io.github.itzispyder.clickcrystals.events.Cancellable;

public class RepeatingTask extends Task implements Cancellable {

    private final int delay;
    private int tickTimer;
    private boolean cancelled;

    public RepeatingTask(Runnable task, int delay) {
        super(task);
        this.delay = delay;
    }

    @Override
    public void onTick() {
        if (cancelled) {
            Scheduler.getTasks().remove(this);
            return;
        }
        if (tickTimer++ < delay) return;
        tickTimer = 0;
        getTask().run();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
