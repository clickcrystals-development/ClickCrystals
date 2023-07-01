package io.github.itzispyder.clickcrystals.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class DrawableUtils {

    public static void drawText(DrawContext context, String text, int x, int y, float scale, boolean shadow) {
        drawText(context, Text.literal(text), x, y, scale, shadow);
    }

    public static void drawText(DrawContext context, String text, int x, int y, boolean shadow) {
        drawText(context, Text.literal(text), x, y, 1.0F, shadow);
    }

    public static void drawText(DrawContext context, Text text, int x, int y, float scale, boolean shadow) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        x *= rescale;
        y *= rescale;

        drawText(context, text, x, y, shadow);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawText(DrawContext context, Text text, int x, int y, boolean shadow) {
        context.drawText(mc.textRenderer, text, x, y, 0xFFFFFFFF, shadow);
    }

    public static void drawLeftText(DrawContext context, String text, int leftX, int y, float scale, boolean shadow) {
        drawText(context, text, leftX - mc.textRenderer.getWidth(text), y, scale, shadow);
    }

    public static void drawLeftText(DrawContext context, String text, int leftX, int y, boolean shadow) {
        drawText(context, text, leftX - mc.textRenderer.getWidth(text), y, 1.0F, shadow);
    }

    public static void drawLeftText(DrawContext context, Text text, int leftX, int y, float scale, boolean shadow) {
        drawText(context, text, leftX - mc.textRenderer.getWidth(text), y, scale, shadow);
    }

    public static void drawLeftText(DrawContext context, Text text, int leftX, int y, boolean shadow) {
        drawText(context, text, leftX - mc.textRenderer.getWidth(text), y, 1.0F, shadow);
    }

    public static void drawCenteredText(DrawContext context, Text text, int centerX, int y, float scale, boolean shadow) {
        drawText(context, text, centerX - mc.textRenderer.getWidth(text) / 2, y, scale, shadow);
    }

    public static void drawCenteredText(DrawContext context, Text text, int centerX, int y, boolean shadow) {
        drawText(context, text, centerX - mc.textRenderer.getWidth(text) / 2, y, 1.0F, shadow);
    }

    public static void drawCenteredText(DrawContext context, String text, int centerX, int y, float scale, boolean shadow) {
        drawText(context, text, centerX - mc.textRenderer.getWidth(text) / 2, y, scale, shadow);
    }

    public static void drawCenteredText(DrawContext context, String text, int centerX, int y, boolean shadow) {
        drawText(context, text, centerX - mc.textRenderer.getWidth(text) / 2, y, 1.0F, shadow);
    }

    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        drawBorder(context, x, y , width, height, 1, color);
    }

    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int thickness, int color) {
        DrawContext dc = new DrawContext(mc, mc.getBufferBuilders().getEntityVertexConsumers());

        dc.fill(x, y, x + width, y + thickness, color);
        dc.fill(x, y + height, x + width, y + height - thickness, color);
        dc.fill(x, y, x + thickness, y + height, color);
        dc.fill(x + width, y, x + width - thickness, y + height, color);
    }
}
