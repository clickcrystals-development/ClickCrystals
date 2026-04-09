package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.CameraClip;
import io.github.itzispyder.clickcrystals.modules.modules.misc.FreeLook;
import io.github.itzispyder.clickcrystals.modules.modules.misc.Zoom;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import net.minecraft.client.Camera;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera implements Global {

    @Unique private boolean bypassCameraClip;

    @Inject(method = "getFluidInCamera", at = @At("RETURN"), cancellable = true)
    public void getSubmersionType(CallbackInfoReturnable<FogType> cir) {
        if (Module.isEnabled(NoOverlay.class)) {
            cir.setReturnValue(FogType.NONE);
        }
    }

    @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(float f, CallbackInfoReturnable<Float> cir) {
        CameraClip clip = Module.get(CameraClip.class);
        if (bypassCameraClip) {
            bypassCameraClip = false;
            return;
        }

        if (clip.isEnabled() && clip.enableCameraClip.getVal()) {
            cir.setReturnValue(clip.clipDistance.getVal().floatValue());
        }
        else if (clip.isEnabled() && clip.clipDistance.getVal() > 0.0) {
            bypassCameraClip = true;
            AccessorCamera ac = (AccessorCamera) this;
            float dist = ac.invokeClipToSpace(clip.clipDistance.getVal().floatValue());
            cir.setReturnValue(dist);
        }
    }

    @ModifyArgs(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"))
    private void onUpdateSetRotationArgs(Args args) {
        FreeLook freeLook = Module.get(FreeLook.class);
        if (freeLook.isEnabled() && mc.options.getCameraType() == freeLook.perspective.getVal().getPerspective()) {
            args.set(0, freeLook.cY);
            args.set(1, freeLook.cP);
        }
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void getFov(CallbackInfoReturnable<Float> cir) {
        Zoom zoom = Module.get(Zoom.class);

        if (zoom.isEnabled() && zoom.isZooming()) {
            cir.setReturnValue(zoom.getZoomMultiplierValue(cir.getReturnValue()));
        }
    }
}
