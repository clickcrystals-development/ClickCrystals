package io.github.itzispyder.clickcrystals.gui.misc.animators;

public final class Animations {

    // f(x) = x
    public static final AnimationController LINEAR = x -> x;

    // f(x) = 1.2sin(2.1555x)
    public static final AnimationController UPWARDS_BOUNCE = x -> 1.2 * Math.sin(2.1555 * x);

    // f(x) = 0.8sin(5x-2.5)+0.5
    public static final AnimationController ELASTIC_BOUNCE = x -> 0.8 * Math.sin(5 * x - 2.5) + 0.5;

    // f(x) = 1 / [1 + e^(6-12x)]
    public static final AnimationController FADE_IN_AND_OUT = x -> 1 / (1 + Math.exp(6 - 12 * x));

    @FunctionalInterface
    public interface AnimationController {

        /**
         * Average f(x) math pun
         */
        double f(double x);
    }
}
