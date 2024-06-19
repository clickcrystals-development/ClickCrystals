package io.github.itzispyder.clickcrystals.gui.misc.animators;

public final class Animations {

    // f(x) = x
    public static final AnimationController LINEAR = x -> x;

    // f(x) = 1.2sin(2.1555x)
    public static final AnimationController POP_BULGE = x -> 1.2 * Math.sin(2.1555 * x);

    @FunctionalInterface
    public interface AnimationController {

        /**
         * Average f(x) math pun
         */
        double f(double x);
    }
}
