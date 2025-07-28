package io.github.itzispyder.clickcrystals.gui.misc.animators;

import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.util.MathUtils;

public class Animator {

    private long start, length;
    private boolean reversed;
    private Animations.AnimationController animationController;

    public Animator(long length, Animations.AnimationController animationController) {
        this.start = System.currentTimeMillis();
        this.length = length;
        this.reversed = false;
        this.animationController = animationController;
    }

    public Animator(long length) {
        this(length, Animations.LINEAR);
    }

    private double getAnimation(double x) {
        if (x <= 0 || x >= 1)
            return x;
        return animationController.f(x); // lmao the f(x) math pun
    }

    public double getAnimation() {
        return getAnimation(getProgressClamped());
    }

    public double getAnimationReversed() {
        return getAnimation(getProgressClampedReversed());
    }

    public Animations.AnimationController getAnimationController() {
        return animationController;
    }

    public void setAnimationController(Animations.AnimationController animationController) {
        this.animationController = animationController;
    }

    public double getProgress() {
        long pass = System.currentTimeMillis() - start;
        double rat = pass / (double)length;
        return reversed ? 1 - rat : rat;
    }

    public double getProgressClamped() {
        return MathUtils.clamp(getProgress(), 0.0, 1.0);
    }

    public double getProgressReversed() {
        return 1 - getProgress();
    }

    public double getProgressClampedReversed() {
        return MathUtils.clamp(getProgressReversed(), 0.0, 1.0);
    }

    public boolean isFinished() {
        double p = getProgress();
        return reversed ? p <= 0.0 : p >= 1.0;
    }

    public void reverse() {
        reversed = !reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void reset(long length) {
        this.start = System.currentTimeMillis();
        this.length = length;
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }

    public static int transformColorOpacity(Animator animator, int hex) {
        return new Color(hex).getHexCustomAlpha(animator.getProgressClamped());
    }

    public long getLength() {
        return length;
    }
}
