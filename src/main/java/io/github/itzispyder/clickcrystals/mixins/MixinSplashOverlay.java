package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui_beta.misc.GuiTextures;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;

@Mixin(SplashOverlay.class)
public abstract class MixinSplashOverlay implements Global {

    @Shadow private float progress;

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V"))
    public void renderLoading(Args args) {
        args.set(5, 0xFF075E74);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clearColor(FFFF)V"))
    public void renderInitializing(Args args) {
        Color color = new Color(0xFF075E74, true);
        float r = color.getRed() / 255.0F;
        float g = color.getGreen() / 255.0F;
        float b = color.getBlue() / 255.0F;
        float a = color.getAlpha() / 255.0F;

        args.set(0, r);
        args.set(1, g);
        args.set(2, b);
        args.set(3, a);
    }

    @Inject(method = "renderProgressBar", at = @At("TAIL"))
    public void renderProgressBar(DrawContext context, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        Tessellator tess = Tessellator.getInstance();
        DrawContext c = new DrawContext(mc, VertexConsumerProvider.immediate(tess.getBuffer()));
        MatrixStack m = c.getMatrices();
        int i = MathHelper.ceil((float)(maxX - minX - 2) * this.progress);
        int w = 50;
        int s = w / 2;
        int x = MathUtils.minMax(minX + i, minX, maxX - s);
        int y = maxY - 50;

        m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(360 * progress), x + s, y + s, 1);
        m.scale(opacity, opacity, opacity);
        c.drawTexture(GuiTextures.ICON, x, y, 0, 0, w, w, w, w);
    }
}
