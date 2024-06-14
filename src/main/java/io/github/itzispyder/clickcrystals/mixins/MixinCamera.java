package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.CameraClip;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class MixinCamera {

    @Unique
    private boolean bypassCameraClip;

    @Shadow
    protected abstract float clipToSpace(float desiredCameraDistance);

    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    public void getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (Module.isEnabled(NoOverlay.class)) {
            cir.setReturnValue(CameraSubmersionType.NONE);
        }
    }

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(float f, CallbackInfoReturnable<Float> cir) {
        if (bypassCameraClip) {
            bypassCameraClip = false;
            return;
        }

        CameraClip clip = Module.get(CameraClip.class);

        if (!clip.isEnabled()) {
            return;
        }

        if (clip.getEnableCameraClipSetting()) {
            cir.setReturnValue(clip.getClipDistanceSetting().floatValue());
        } else if (clip.getClipDistanceSetting() > 0.0) {
            bypassCameraClip = true;
            cir.setReturnValue(clipToSpace(clip.getClipDistanceSetting().floatValue()));
        }
    }
}
