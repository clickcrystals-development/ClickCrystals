package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.CameraUtils;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
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
    private double clipToSpace(double desiredCameraDistance) {
        return 0;
    }

    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    public void getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) {
            cir.setReturnValue(CameraSubmersionType.NONE);
        }
    }

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
        if (bypassCameraClip) {
            bypassCameraClip = false;
        } else {
            CameraUtils cameraUtils = Module.get(CameraUtils.class);

            if (cameraUtils.isEnabled()) {
                if (cameraUtils.getEnableCameraClipSetting()) {
                    info.setReturnValue(cameraUtils.getClipDistanceSetting());
                } else {
                    if (cameraUtils.getClipDistanceSetting() > 0.0) {
                        bypassCameraClip = true;
                        info.setReturnValue(clipToSpace(cameraUtils.getClipDistanceSetting()));
                    }
                }
            }
        }
    }
}
