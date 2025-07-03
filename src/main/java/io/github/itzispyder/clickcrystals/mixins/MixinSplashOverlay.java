package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderConstants;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3x2f;
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

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Matrix3x2fStack matrices = context.getMatrices();

        int i = MathHelper.ceil((float)(maxX - minX - 2) * this.progress);
        int size = 50;
        int halfSize = size / 2;
        int x = MathUtils.clamp(minX + i, minX, maxX - halfSize);
        int y = maxY - 50;

        Matrix3x2f mat = matrices.pushMatrix();

        matrices.translate(x + halfSize, y + halfSize);
//        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(360 * progress));
        matrices.rotate((float)(2 * Math.PI * progress));
        matrices.scaleAround(opacity, x, y);
        matrices.translate(-(x + halfSize), -(y + halfSize));

        buf.vertex(mat, x, y, 0).texture(0, 0).color(-1);
        buf.vertex(mat, x, y + size, 0).texture(0, 1).color(-1);
        buf.vertex(mat, x + size, y + size, 0).texture(1, 1).color(-1);
        buf.vertex(mat, x + size, y, 0).texture(1, 0).color(-1);

        RenderUtils.drawBuffer(buf, RenderConstants.TEX_QUADS.apply(Tex.ICON));
        matrices.popMatrix();
    }
}
