package io.github.itzispyder.clickcrystals.scheduler;

public abstract class Task {

    private final Runnable task;

    public Task(Runnable task) {
        this.task = task;
    }

    public Runnable getTask() {
        return task;
    }

    public abstract void onTick();
}
