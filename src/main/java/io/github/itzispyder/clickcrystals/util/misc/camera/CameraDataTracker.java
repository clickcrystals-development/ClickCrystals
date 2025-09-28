package io.github.itzispyder.clickcrystals.util.misc.camera;

import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;

public class CameraDataTracker {

    private static final int trackerCapacity = 100;
    private static final int sampleCapacity = 10;

    private final float[] trackPitch, trackYaw;
    private int trackIndex;
    private float prevPitch, prevYaw;
    private boolean ready;

    public CameraDataTracker() {
        trackPitch = new float[trackerCapacity];
        trackYaw = new float[trackerCapacity];
    }

    public void onTick() {
        if (PlayerUtils.invalid())
            return;

        ClientPlayerEntity p = PlayerUtils.player();
        float pitch = p.getPitch();
        float yaw = p.getYaw();
        recordData(pitch, yaw);
    }

    public void recordData(float pitch, float yaw) {
        if (!ready) {
            prevPitch = pitch;
            prevYaw = yaw;
            ready = true;
        }

        trackPitch[trackIndex % trackerCapacity] = Math.abs(pitch - prevPitch);
        trackYaw[trackIndex % trackerCapacity] = Math.abs(yaw - prevYaw);
        prevPitch = pitch;
        prevYaw = yaw;
        trackIndex++;
    }

    public float getSensX() {
        return sample(trackYaw, 1);
    }

    public float getSensX(float mul) {
        return sample(trackYaw, mul);
    }

    public float getSensY() {
        return sample(trackPitch, 1);
    }

    public float getSensY(float mul) {
        return sample(trackPitch, mul);
    }

    private float sample(float[] data, float mul) {
        float sum = 0;
        for (int i = 0; i < sampleCapacity; i++) {
            int index = (i - (sampleCapacity - 1) + trackIndex) % trackerCapacity;
            sum += data[index];
        }
        return sum / sampleCapacity * mul;
    }
}
