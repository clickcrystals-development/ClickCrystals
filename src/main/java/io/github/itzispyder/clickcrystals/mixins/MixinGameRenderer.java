package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoHurtCam;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoViewBob;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    public void onHurtViewTilt(CameraRenderState cameraState, PoseStack poseStack, CallbackInfo ci) {
        if (Module.isEnabled(NoHurtCam.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void onViewBob(CameraRenderState cameraState, PoseStack poseStack, CallbackInfo ci) {
        if (Module.isEnabled(NoViewBob.class)) {
            ci.cancel();
        }
    }
}
