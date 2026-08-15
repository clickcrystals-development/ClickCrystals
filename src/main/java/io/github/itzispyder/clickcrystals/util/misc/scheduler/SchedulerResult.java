package io.github.itzispyder.clickcrystals.util.misc.scheduler;

import java.util.concurrent.ScheduledFuture;

public class SchedulerResult {

    private final ScheduledFuture<?> future;
    private final Runnable onCancel;

    public SchedulerResult(ScheduledFuture<?> future, Runnable onCancel) {
        this.future = future;
        this.onCancel = onCancel;
    }

    public SchedulerResult(ScheduledFuture<?> future) {
        this(future, null);
    }

    public void cancel() {
        future.cancel(true);

        if (onCancel != null)
            onCancel.run();
    }
}
