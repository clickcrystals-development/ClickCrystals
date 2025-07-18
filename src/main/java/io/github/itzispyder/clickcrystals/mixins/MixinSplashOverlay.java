package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SplashOverlay.class)
public abstract class MixinSplashOverlay implements Global {

    @Shadow private float progress;

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    public void renderLoading(Args args) {
        if (ClickCrystals.config.isDisableCustomLoading())
            return;

        args.set(4, 0xFF075E74);
    }

//    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clearColor(FFFF)V"))
//    public void renderInitializing(Args args) {
//        if (ClickCrystals.config.isDisableCustomLoading())
//            return;
//
//        Color color = new Color(0xFF075E74, true);
//        float r = color.getRed() / 255.0F;
//        float g = color.getGreen() / 255.0F;
//        float b = color.getBlue() / 255.0F;
//        float a = color.getAlpha() / 255.0F;
//
//        args.set(0, r);
//        args.set(1, g);
//        args.set(2, b);
//        args.set(3, a);
//    }

    @Inject(method = "renderProgressBar", at = @At("TAIL"))
    public void renderProgressBar(DrawContext context, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        if (ClickCrystals.config.isDisableCustomLoading())
            return;

        int i = MathHelper.ceil((float)(maxX - minX - 2) * this.progress);
        int size = (int)(50 * opacity);
        int halfSize = size / 2;
        int x = MathUtils.clamp(minX + i, minX, maxX);
        int y = maxY - 50;

        Matrix3x2fStack matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.rotateAbout((float)(2 * Math.PI * progress), x, y);

        RenderUtils.drawTexture(context, Tex.ICON, x - halfSize, y - halfSize, size, size);

        matrices.popMatrix();
    }
}
