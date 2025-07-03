package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemPopScale;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameOverlayRenderer.class)
public class MixinInGameOverlayRenderer {

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
