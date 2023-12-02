package io.github.itzispyder.clickcrystals.util.minecraft;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class RenderUtils {

    // default text

    public static void drawDefaultScaledText(MatrixStack context, Text text, int x, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context;
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        x *= rescale;
        y *= rescale;

        drawDefaultText(context, text, x, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultCenteredScaledText(MatrixStack context, Text text, int centerX, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context;
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        centerX *= rescale;
        centerX = centerX - (mc.textRenderer.getWidth(text) / 2);
        y *= rescale;

        drawDefaultText(context, text, centerX, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultRightScaledText(MatrixStack context, Text text, int rightX, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context;
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        rightX *= rescale;
        rightX = rightX - mc.textRenderer.getWidth(text);
        y *= rescale;

        drawDefaultText(context, text, rightX, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultScaledText(MatrixStack context, Text text, int x, int y, float scale, boolean shadow) {
        drawDefaultScaledText(context, text, x, y, scale, shadow, 0xFFFFFFFF);
    }

    public static void drawDefaultCenteredScaledText(MatrixStack context, Text text, int centerX, int y, float scale, boolean shadow) {
        drawDefaultCenteredScaledText(context, text, centerX, y, scale, shadow, 0xFFFFFFFF);
    }

    public static void drawDefaultRightScaledText(MatrixStack context, Text text, int rightX, int y, float scale, boolean shadow) {
        drawDefaultRightScaledText(context, text, rightX, y, scale, shadow, 0xFFFFFFFF);
    }

    public static void drawDefaultText(MatrixStack context, Text text, int x, int y, boolean shadow, int color) {
        if (shadow) {
            DrawableHelper.drawTextWithShadow(context, mc.textRenderer, text, x, y, color);
        }
        else {
            mc.textRenderer.draw(context, text, x, y, color);
        }
    }

    // non-default
    // draw normal text

    public static void drawText(MatrixStack context, String text, int x, int y, float scale, boolean shadow) {
        drawDefaultScaledText(context, Text.literal(text), x, y, scale, shadow);
    }

    public static void drawText(MatrixStack context, String text, int x, int y, boolean shadow) {
        drawDefaultScaledText(context, Text.literal(text), x, y, 1.0F, shadow);
    }

    // draw left text

    public static void drawRightText(MatrixStack context, String text, int leftX, int y, float scale, boolean shadow) {
        drawDefaultRightScaledText(context, Text.literal(text), leftX, y, scale, shadow);
    }

    public static void drawRightText(MatrixStack context, String text, int leftX, int y, boolean shadow) {
        drawDefaultRightScaledText(context, Text.literal(text), leftX, y, 1.0F, shadow);
    }

    public static void drawRightText(MatrixStack context, Text text, int leftX, int y, float scale, boolean shadow) {
        drawDefaultRightScaledText(context, text, leftX, y, scale, shadow);
    }

    public static void drawRightText(MatrixStack context, Text text, int leftX, int y, boolean shadow) {
        drawDefaultRightScaledText(context, text, leftX, y, 1.0F, shadow);
    }

    // draw centered text

    public static void drawCenteredText(MatrixStack context, String text, int centerX, int y, float scale, boolean shadow) {
        drawDefaultCenteredScaledText(context, Text.literal(text), centerX, y, scale, shadow);
    }

    public static void drawCenteredText(MatrixStack context, String text, int centerX, int y, boolean shadow) {
        drawDefaultCenteredScaledText(context, Text.literal(text), centerX, y, 1.0F, shadow);
    }

    public static void drawCenteredText(MatrixStack context, Text text, int centerX, int y, float scale, boolean shadow) {
        drawDefaultCenteredScaledText(context, text, centerX, y, scale, shadow);
    }

    public static void drawCenteredText(MatrixStack context, Text text, int centerX, int y, boolean shadow) {
        drawDefaultCenteredScaledText(context, text, centerX, y, 1.0F, shadow);
    }

    // draw borders and lines

    public static void drawBorder(MatrixStack context, int x, int y, int width, int height, int color) {
        drawBorder(context, x, y , width, height, 1, color);
    }

    public static void drawBorder(MatrixStack context, int x, int y, int width, int height, int thickness, int color) {
        drawHorizontalLine(context, x, y, width, thickness, color);
        drawVerticalLine(context, x, y + thickness, height - 2, thickness, color);
        drawVerticalLine(context, x + width - thickness, y + thickness, height - 2, thickness, color);
        drawHorizontalLine(context, x, y + height - thickness, width, thickness, color);
    }

    public static void drawHorizontalLine(MatrixStack context, int x, int y, int length, int thickness, int color) {
        fill(context, x, y, length, thickness, color);
    }

    public static void drawVerticalLine(MatrixStack context, int x, int y, int length, int thickness, int color) {
        fill(context, x, y, thickness, length, color);
    }

    public static void fill(MatrixStack context, int x, int y, int width, int height, int color) {
        DrawableHelper.fill(context, x, y, x + width, y + height, color);
    }

    public static void fillGradient(MatrixStack context, int x, int y, int width, int height, int colorTop, int colorBottom) {
        try {
            Class<?>[] params = { MatrixStack.class, int.class, int.class, int.class, int.class, int.class, int.class };
            Method method = DrawableHelper.class.getDeclaredMethod("fillGradient", params);
            method.invoke(null, context, x, y, width, height, colorTop, colorBottom);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {}
    }

    public static void drawCross(MatrixStack context, int x, int y, int width, int height, int color) {
        int len = (int)Math.sqrt(width * width + height * height);
        float deg = MathUtils.tanInverse((double)height / (double)width);

        context.push();
        context.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(deg), x, y, 0);
        drawHorizontalLine(context, x, y, len, 1, color);
        context.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(deg), x, y, 0);
        context.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180.0F + deg), x + width, y, 0);
        drawHorizontalLine(context, x + width, y, len, 1, color);
        context.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F + deg), x + width, y, 0);
        context.pop();
    }

    // misc

    public static void drawTexture(MatrixStack context, Identifier texture, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(context, x, y, 0, 0, width, height, width, height);
    }

    public static void drawItem(MatrixStack context, ItemStack item, int x, int y, float scale) {
        x /= scale;
        y /= scale;
        context.push();
        context.scale(scale, scale, scale);
        mc.getItemRenderer().renderGuiItemIcon(context, item, x, y);
        mc.getItemRenderer().renderGuiItemOverlay(context, mc.textRenderer, item, x, y);
        context.pop();
    }

    public static void drawItem(MatrixStack context, ItemStack item, int x, int y, float scale, String text) {
        x /= scale;
        y /= scale;
        context.push();
        context.scale(scale, scale, scale);
        mc.getItemRenderer().renderGuiItemIcon(context, item, x, y);
        mc.getItemRenderer().renderGuiItemOverlay(context, mc.textRenderer, item, x, y, text);
        context.pop();
    }

    public static void drawItem(MatrixStack context, ItemStack item, int x, int y, int size) {
        drawItem(context, item, x, y, size / 16.0F);
    }

    public static void drawItem(MatrixStack context, ItemStack item, int x, int y) {
        drawItem(context, item, x, y, 1.0F);
    }
}
