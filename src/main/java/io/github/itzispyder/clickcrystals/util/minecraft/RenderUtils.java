package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public final class RenderUtils implements Global {

    // default text

    public static void drawDefaultScaledText(DrawContext context, Text text, int x, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        x *= rescale;
        y *= rescale;

        drawDefaultText(context, text, x, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultCenteredScaledText(DrawContext context, Text text, int centerX, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        centerX *= rescale;
        centerX = centerX - (mc.textRenderer.getWidth(text) / 2);
        y *= rescale;

        drawDefaultText(context, text, centerX, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultRightScaledText(DrawContext context, Text text, int rightX, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        rightX *= rescale;
        rightX = rightX - mc.textRenderer.getWidth(text);
        y *= rescale;

        drawDefaultText(context, text, rightX, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultScaledText(DrawContext context, Text text, int x, int y, float scale, boolean shadow) {
        drawDefaultScaledText(context, text, x, y, scale, shadow, 0xFFFFFFFF);
    }

    public static void drawDefaultCenteredScaledText(DrawContext context, Text text, int centerX, int y, float scale, boolean shadow) {
        drawDefaultCenteredScaledText(context, text, centerX, y, scale, shadow, 0xFFFFFFFF);
    }

    public static void drawDefaultRightScaledText(DrawContext context, Text text, int rightX, int y, float scale, boolean shadow) {
        drawDefaultRightScaledText(context, text, rightX, y, scale, shadow, 0xFFFFFFFF);
    }

    public static void drawDefaultText(DrawContext context, Text text, int x, int y, boolean shadow, int color) {
        context.drawText(mc.textRenderer, text, x, y, color, shadow);
    }

    // non-default
    // draw normal text

    public static void drawText(DrawContext context, String text, int x, int y, float scale, boolean shadow) {
        drawDefaultScaledText(context, Text.literal(text), x, y, scale, shadow);
    }

    public static void drawText(DrawContext context, String text, int x, int y, boolean shadow) {
        drawDefaultScaledText(context, Text.literal(text), x, y, 1.0F, shadow);
    }

    // draw left text

    public static void drawRightText(DrawContext context, String text, int leftX, int y, float scale, boolean shadow) {
        drawDefaultRightScaledText(context, Text.literal(text), leftX, y, scale, shadow);
    }

    public static void drawRightText(DrawContext context, String text, int leftX, int y, boolean shadow) {
        drawDefaultRightScaledText(context, Text.literal(text), leftX, y, 1.0F, shadow);
    }

    public static void drawRightText(DrawContext context, Text text, int leftX, int y, float scale, boolean shadow) {
        drawDefaultRightScaledText(context, text, leftX, y, scale, shadow);
    }

    public static void drawRightText(DrawContext context, Text text, int leftX, int y, boolean shadow) {
        drawDefaultRightScaledText(context, text, leftX, y, 1.0F, shadow);
    }

    // draw centered text

    public static void drawCenteredText(DrawContext context, String text, int centerX, int y, float scale, boolean shadow) {
        drawDefaultCenteredScaledText(context, Text.literal(text), centerX, y, scale, shadow);
    }

    public static void drawCenteredText(DrawContext context, String text, int centerX, int y, boolean shadow) {
        drawDefaultCenteredScaledText(context, Text.literal(text), centerX, y, 1.0F, shadow);
    }

    public static void drawCenteredText(DrawContext context, Text text, int centerX, int y, float scale, boolean shadow) {
        drawDefaultCenteredScaledText(context, text, centerX, y, scale, shadow);
    }

    public static void drawCenteredText(DrawContext context, Text text, int centerX, int y, boolean shadow) {
        drawDefaultCenteredScaledText(context, text, centerX, y, 1.0F, shadow);
    }

    // draw borders and lines

    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        drawBorder(context, x, y , width, height, 1, color);
    }

    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int thickness, int color) {
        drawHorizontalLine(context, x, y, width, thickness, color);
        drawVerticalLine(context, x, y + thickness, height - 2, thickness, color);
        drawVerticalLine(context, x + width - thickness, y + thickness, height - 2, thickness, color);
        drawHorizontalLine(context, x, y + height - thickness, width, thickness, color);
    }

    public static void drawHorizontalLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        fill(context, x, y, length, thickness, color);
    }

    public static void drawVerticalLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        fill(context, x, y, thickness, length, color);
    }

    public static void fill(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public static void fillGradient(DrawContext context, int x, int y, int width, int height, int colorTop, int colorBottom) {
        context.fillGradient(x, y, x + width, y + height, colorTop, colorBottom);
    }

    public static void drawCross(DrawContext context, int x, int y, int width, int height, int color) {
        int len = (int)Math.sqrt(width * width + height * height);
        float deg = MathUtils.tanInverse((double)height / (double)width);

        context.getMatrices().push();
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(deg), x, y, 0);
        drawHorizontalLine(context, x, y, len, 1, color);
        context.getMatrices().multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(deg), x, y, 0);
        context.getMatrices().multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180.0F + deg), x + width, y, 0);
        drawHorizontalLine(context, x + width, y, len, 1, color);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F + deg), x + width, y, 0);
        context.getMatrices().pop();
    }

    // misc

    public static void drawTexture(DrawContext context, Identifier texture, int x, int y, int width, int height) {
        context.drawTexture(texture, x, y, 0, 0, width, height, width, height);
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, float scale) {
        x /= scale;
        y /= scale;
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        context.drawItem(item, x, y);
        context.drawItemInSlot(mc.textRenderer, item, x, y);
        context.getMatrices().pop();
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, float scale, String text) {
        x /= scale;
        y /= scale;
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        context.drawItem(item, x, y);
        context.drawItemInSlot(mc.textRenderer, item, x, y, text);
        context.getMatrices().pop();
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, int size) {
        drawItem(context, item, x, y, size / 16.0F);
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y) {
        drawItem(context, item, x, y, 1.0F);
    }

    public static DrawContext createContext() {
        return new DrawContext(MinecraftClient.getInstance(), VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer()));
    }

    public static int winWidth() {
        return mc.getWindow().getScaledWidth();
    }

    public static int winHeight() {
        return mc.getWindow().getScaledHeight();
    }
}
