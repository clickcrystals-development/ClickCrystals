package io.github.itzispyder.clickcrystals.gui.misc.animators;

import java.util.function.BooleanSupplier;

public class PollingAnimator extends Animator {

    private final BooleanSupplier poll;
    private boolean pollSuccess;

    public PollingAnimator(int length, BooleanSupplier poll) {
        super(length);
        this.poll = poll;

        boolean bool = poll.getAsBoolean();
        this.pollSuccess = bool;
        this.setReversed(!bool);
    }

    @Override
    public double getProgress() {
        poll();
        return super.getProgress();
    }

    public void poll() {
        if (poll.getAsBoolean() && !pollSuccess) {
            pollSuccess = true;
            this.setReversed(false);
            this.reset();
        }
        else if (!poll.getAsBoolean() && pollSuccess) {
            pollSuccess = false;
            this.setReversed(true);
            this.reset();
        }
    }
}
