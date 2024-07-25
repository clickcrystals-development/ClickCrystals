package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.CameraClip;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.FreeLook;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class MixinCamera implements Global {

    @Unique
    private boolean bypassCameraClip;

    @Shadow
    protected abstract float clipToSpace(float desiredCameraDistance);

    @Shadow
    protected abstract void setRotation(float y, float p);


    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    public void getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (Module.isEnabled(NoOverlay.class)) {
            cir.setReturnValue(CameraSubmersionType.NONE);
        }
    }

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(float f, CallbackInfoReturnable<Float> cir) {
        CameraClip clip = Module.get(CameraClip.class);
        if (bypassCameraClip) {
            bypassCameraClip = false;
            return;
        }
        if (clip.isEnabled() && clip.enableCameraClip.getVal()) {
            cir.setReturnValue(clip.clipDistance.getVal().floatValue());
        } else if (clip.clipDistance.getVal() > 0.0) {
            bypassCameraClip = true;
            cir.setReturnValue(clipToSpace(clip.clipDistance.getVal().floatValue()));
        }
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void redirectSetRotation(Camera camera, float yaw, float pitch) {
        FreeLook freeLook = Module.get(FreeLook.class);
        if (freeLook.isEnabled() && mc.options.getPerspective() == freeLook.PerspectivePoint.getVal().getPerspective()) {
            return;
        }
        this.setRotation(yaw, pitch);
    }
}