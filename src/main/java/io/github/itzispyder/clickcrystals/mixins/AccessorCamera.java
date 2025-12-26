package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface AccessorCamera {

    @Invoker("setRotation")
    void invokeSetRotation(float yaw, float pitch);

    @Invoker("clipToSpace")
    float invokeClipToSpace(float desiredCameraDistance);
}
