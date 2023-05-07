package io.github.itzispyder.clickcrystals.util;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class DrawableUtils {

    public static void drawText(MatrixStack matrices, String text, int x, int y, boolean shadow) {
        drawText(matrices, Text.literal(text), x, y, shadow);
    }

    public static void drawText(MatrixStack matrices, Text text, int x, int y, boolean shadow) {
        if (shadow) {
            mc.textRenderer.drawWithShadow(matrices, text, x, y, 0xFFFFFFFF);
        }
        else {
            mc.textRenderer.draw(matrices, text, x, y, 0xFFFFFFFF);
        }
    }

    public static void drawCenteredText(MatrixStack matrices, Text text, int x, int y, boolean shadow) {
        drawText(matrices, text, x - mc.textRenderer.getWidth(text) / 2, y, shadow);
    }

    public static void drawCenteredText(MatrixStack matrices, String text, int x, int y, boolean shadow) {
        drawText(matrices, text, x - mc.textRenderer.getWidth(text) / 2, y, shadow);
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
