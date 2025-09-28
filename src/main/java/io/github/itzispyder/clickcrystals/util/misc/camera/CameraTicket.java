package io.github.itzispyder.clickcrystals.util.misc.camera;

import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.util.MathUtils;

public class CameraTicket {

    public final float destPitch, destYaw;
    public final float startPitch, startYaw;
    public final float sensX, sensY;

    private final CameraRotator owner;
    private final Animations.AnimationController progressFunction;
    private Displacement displacementPitch, displacementYaw;
    private final boolean lockCursor;

    public CameraTicket(CameraRotator owner, Animations.AnimationController progressFunction, float pitch, float yaw, float sensX, float sensY, boolean lockCursor) {
        this.destPitch = pitch;
        this.destYaw = yaw;
        this.sensX = sensX;
        this.sensY = sensY;
        this.startPitch = owner.getPitch();
        this.startYaw = owner.getYaw();
        this.progressFunction = progressFunction;
        this.owner = owner;
        this.lockCursor = lockCursor;
    }

    public void open() {
        this.displacementPitch = Displacement.fromAngles(startPitch, destPitch, sensY);
        this.displacementYaw = Displacement.fromAngles(startYaw, destYaw, sensX);

        float len = displacementYaw.getTimeLen();
        if (len >= 5)
            displacementPitch.setSensitivity(displacementPitch.displacement / len);

        if (lockCursor)
            owner.lockCursor();
    }

    public void close() {
        displacementPitch = displacementYaw = null;
    }

    public float getProgressivePitch() {
        if (isExpired())
            throw new IllegalArgumentException("CameraTicket is not open!");

        return interpolate(startPitch,
                startPitch + displacementPitch.displacement * displacementPitch.direction,
                displacementPitch.getProgress());
    }

    public float getProgressiveYaw() {
        if (isExpired())
            throw new IllegalArgumentException("CameraTicket is not open!");

        return interpolate(startYaw,
                startYaw + displacementYaw.displacement * displacementYaw.direction,
                displacementYaw.getProgress());
    }

    private float interpolate(float a, float b, float progress) {
        return a + (b - a) * (float) progressFunction.f(MathUtils.clamp(progress, 0, 1));
    }

    public boolean isOpen() {
        return displacementPitch != null && displacementYaw != null;
    }

    public boolean isClosed() {
        return !isOpen();
    }

    public boolean isExpired() {
        return isOpen() && (displacementPitch.getProgress() > 1 && displacementYaw.getProgress() > 1);
    }

    public Displacement getDisplacementPitch() {
        return displacementPitch;
    }

    public Displacement getDisplacementYaw() {
        return displacementYaw;
    }
}
