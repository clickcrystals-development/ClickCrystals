package io.github.itzispyder.clickcrystals.scripting.runtime;

/**
 * Converts debounced keyboard edges and client ticks to tap / hold events.
 * Does not poll global input or send artificial key events.
 */
public final class KeyGestureTracker {
    public enum Signal { NONE, TAP, HOLD_START, HOLD_REPEAT }

    private final int thresholdTicks;
    private final int repeatTicks;
    private boolean down;
    private boolean holding;
    private int elapsed;

    public KeyGestureTracker(int thresholdTicks, int repeatTicks) {
        if (thresholdTicks < 1 || thresholdTicks > 100)
            throw new IllegalArgumentException("Hold threshold must be 1..100 ticks");
        if (repeatTicks < 1 || repeatTicks > 100)
            throw new IllegalArgumentException("Repeat interval must be 1..100 ticks");
        this.thresholdTicks = thresholdTicks;
        this.repeatTicks = repeatTicks;
    }

    public void press() {
        if (down) return; // ignore OS key auto-repeat
        down=true;
        holding=false;
        elapsed=0;
    }

    public Signal release() {
        if (!down) return Signal.NONE;
        boolean wasHold=holding;
        down=false;
        holding=false;
        elapsed=0;
        return wasHold ? Signal.NONE : Signal.TAP;
    }

    public Signal tick() {
        if (!down) return Signal.NONE;
        elapsed++;
        if (!holding && elapsed >= thresholdTicks) {
            holding=true;
            return Signal.HOLD_START;
        }
        if (holding && (elapsed - thresholdTicks) % repeatTicks == 0)
            return Signal.HOLD_REPEAT;
        return Signal.NONE;
    }

    public void cancel() {
        down=false;
        holding=false;
        elapsed=0;
    }

    public boolean isDown() { return down; }
    public boolean isHolding() { return holding; }
}
