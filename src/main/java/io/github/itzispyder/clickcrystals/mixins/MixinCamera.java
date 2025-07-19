package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.CameraClip;
import io.github.itzispyder.clickcrystals.modules.modules.misc.FreeLook;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera implements Global {

    @Unique private boolean bypassCameraClip;
    @Shadow protected abstract float clipToSpace(float desiredCameraDistance);

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
        } else if (clip.isEnabled() && clip.clipDistance.getVal() > 0.0) {
            bypassCameraClip = true;
            cir.setReturnValue(clipToSpace(clip.clipDistance.getVal().floatValue()));
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void onUpdateSetRotationArgs(Args args) {
        FreeLook freeLook = Module.get(FreeLook.class);
        if (freeLook.isEnabled() && mc.options.getPerspective() == freeLook.perspective.getVal().getPerspective()) {
            args.set(0, freeLook.cY);
            args.set(1, freeLook.cP);
        }
    }
}
