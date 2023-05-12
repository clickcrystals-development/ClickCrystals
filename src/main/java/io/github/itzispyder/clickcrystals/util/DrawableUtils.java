package io.github.itzispyder.clickcrystals.util;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class DrawableUtils {

    public static void drawText(MatrixStack matrices, String text, int x, int y, float scale, boolean shadow) {
        drawText(matrices, Text.literal(text), x, y, scale, shadow);
    }

    public static void drawText(MatrixStack matrices, String text, int x, int y, boolean shadow) {
        drawText(matrices, Text.literal(text), x, y, 1.0F, shadow);
    }

    public static void drawText(MatrixStack matrices, Text text, int x, int y, float scale, boolean shadow) {
        final float rescale = 1 / scale;

        x *= rescale;
        y *= rescale;
        matrices.scale(scale, scale, scale);

        if (shadow) {
            mc.textRenderer.drawWithShadow(matrices, text, x, y, 0xFFFFFFFF);
        }
        else {
            mc.textRenderer.draw(matrices, text, x, y, 0xFFFFFFFF);
        }

        matrices.scale(rescale, rescale, rescale);
    }

    public static void drawLeftText(MatrixStack matrices, String text, int leftX, int y, float scale, boolean shadow) {
        drawText(matrices, text, leftX - mc.textRenderer.getWidth(text), y, scale, shadow);
    }

    public static void drawLeftText(MatrixStack matrices, String text, int leftX, int y, boolean shadow) {
        drawText(matrices, text, leftX - mc.textRenderer.getWidth(text), y, 1.0F, shadow);
    }

    public static void drawLeftText(MatrixStack matrices, Text text, int leftX, int y, float scale, boolean shadow) {
        drawText(matrices, text, leftX - mc.textRenderer.getWidth(text), y, scale, shadow);
    }

    public static void drawLeftText(MatrixStack matrices, Text text, int leftX, int y, boolean shadow) {
        drawText(matrices, text, leftX - mc.textRenderer.getWidth(text), y, 1.0F, shadow);
    }

    public static void drawCenteredText(MatrixStack matrices, Text text, int centerX, int y, float scale, boolean shadow) {
        drawText(matrices, text, centerX - mc.textRenderer.getWidth(text) / 2, y, scale, shadow);
    }

    public static void drawCenteredText(MatrixStack matrices, Text text, int centerX, int y, boolean shadow) {
        drawText(matrices, text, centerX - mc.textRenderer.getWidth(text) / 2, y, 1.0F, shadow);
    }

    public static void drawCenteredText(MatrixStack matrices, String text, int centerX, int y, float scale, boolean shadow) {
        drawText(matrices, text, centerX - mc.textRenderer.getWidth(text) / 2, y, scale, shadow);
    }

    public static void drawCenteredText(MatrixStack matrices, String text, int centerX, int y, boolean shadow) {
        drawText(matrices, text, centerX - mc.textRenderer.getWidth(text) / 2, y, 1.0F, shadow);
    }

    public static void drawBorder(MatrixStack matrices, int x, int y, int width, int height, int color) {
        drawBorder(matrices, x, y , width, height, 1, color);
    }

    public static void drawBorder(MatrixStack matrices, int x, int y, int width, int height, int thickness, int color) {
        DrawableHelper.fill(matrices, x, y, x + width, y + thickness, color);
        DrawableHelper.fill(matrices, x, y + height, x + width, y + height - thickness, color);
        DrawableHelper.fill(matrices, x, y, x + thickness, y + height, color);
        DrawableHelper.fill(matrices, x + width, y, x + width - thickness, y + height, color);
    }
}
