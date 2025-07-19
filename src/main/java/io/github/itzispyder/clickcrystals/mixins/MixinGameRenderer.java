package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.Zoom;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoHurtCam;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoViewBob;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void onHurtViewTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (Module.isEnabled(NoHurtCam.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void onViewBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (Module.isEnabled(NoViewBob.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        Zoom zoom = Module.get(Zoom.class);

        if (zoom.isEnabled() && zoom.isZooming()) {
            cir.setReturnValue(zoom.getZoomMultiplierValue(cir.getReturnValue()));
        }
    }
}
