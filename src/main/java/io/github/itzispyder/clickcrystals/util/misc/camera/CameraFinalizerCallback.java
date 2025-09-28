package io.github.itzispyder.clickcrystals.util.misc.camera;

@FunctionalInterface
public interface CameraFinalizerCallback {

    void onFinish(float pitch, float yaw, CameraRotator rotator);
}
