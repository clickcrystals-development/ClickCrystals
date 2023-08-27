package io.github.itzispyder.clickcrystals.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class RenderUtils {

    // default text

    public static void drawDefaultScaledText(DrawContext context, Text text, int x, int y, float scale, boolean shadow) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        x *= rescale;
        y *= rescale;

        drawDefaultText(context, text, x, y, shadow);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultCenteredScaledText(DrawContext context, Text text, int centerX, int y, float scale, boolean shadow) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        centerX *= rescale;
        centerX = centerX - (mc.textRenderer.getWidth(text) / 2);
        y *= rescale;

        drawDefaultText(context, text, centerX, y, shadow);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultRightScaledText(DrawContext context, Text text, int rightX, int y, float scale, boolean shadow) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        rightX *= rescale;
        rightX = rightX - mc.textRenderer.getWidth(text);
        y *= rescale;

        drawDefaultText(context, text, rightX, y, shadow);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultText(DrawContext context, Text text, int x, int y, boolean shadow) {
        context.drawText(mc.textRenderer, text, x, y, 0xFFFFFFFF, shadow);
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
        context.fill(x, y, x + length, y + thickness, color);
    }

    public static void drawVerticalLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        context.fill(x, y, x + thickness, y + length, color);
    }

    public static void fill(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public static DrawContext createContext() {
        return new DrawContext(MinecraftClient.getInstance(), VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer()));
    }
}
