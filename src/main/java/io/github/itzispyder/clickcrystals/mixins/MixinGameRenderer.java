package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoHurtCam;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoViewBob;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemPopScale;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.Zoom;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void onHurtViewTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        final Module noHurtCam = Module.get(NoHurtCam.class);

        if (noHurtCam.isEnabled()) ci.cancel();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void onViewBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        final Module noViewBob = Module.get(NoViewBob.class);

        if (noViewBob.isEnabled()) ci.cancel();
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        final Zoom zoom = Module.get(Zoom.class);

        if (zoom.isEnabled() && zoom.isZooming()) {
            cir.setReturnValue(zoom.getZoomMultiplierValue(cir.getReturnValue()));
        }
    }

    @ModifyArgs(method = "renderFloatingItem", at = @At(target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", value = "INVOKE"))
    public void renderFloatingItemScaled(Args args) {
        if (!Module.isEnabled(TotemPopScale.class)) {
            return;
        }

        double scale = Module.get(TotemPopScale.class).scale.getVal();
        float f = (float)scale;
        args.set(0, (float)args.get(0) * f);
        args.set(1, (float)args.get(1) * f);
        args.set(2, (float)args.get(2) * f);
    }

    @ModifyArgs(method = "renderFloatingItem", at = @At(target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", value = "INVOKE"))
    public void renderFloatingItemTranslated(Args args) {
        if (!Module.isEnabled(TotemPopScale.class)) {
            return;
        }

        TotemPopScale tps = Module.get(TotemPopScale.class);
        double deltaX = tps.translateX.getVal();
        double deltaY = tps.translateY.getVal();
        float x = (float)deltaX;
        float y = (float)deltaY;
        args.set(0, (float)args.get(0) + x);
        args.set(1, (float)args.get(1) - y);
    }
}
