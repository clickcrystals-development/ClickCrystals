package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import static com.mojang.blaze3d.systems.RenderSystem.*;

public final class RenderUtils implements Global {

    // fill

    public static void fillRect(DrawContext context, int x, int y, int w, int h, int color) {
        BufferBuilder buf = getBuffer();
        Matrix4f mat = context.getMatrices().peek().getPositionMatrix();

        buf.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buf.vertex(mat, (float)x, (float)y, 0).color(color).next();
        buf.vertex(mat, (float)(x + w), (float)y, 0).color(color).next();
        buf.vertex(mat, (float)(x + w), (float)(y + h), 0).color(color).next();
        buf.vertex(mat, (float)x, (float)(y + h), 0).color(color).next();

        setupRender();
        BufferRenderer.drawWithGlobalProgram(buf.end());
        endRender();
    }

    public static void fillArc(DrawContext context, int cX, int cY, int radius, int start, int end, int color) {
        BufferBuilder buf = getBuffer();
        Matrix4f mat = context.getMatrices().peek().getPositionMatrix();

        buf.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        buf.vertex(mat, (float)cX, (float)cY, 0).color(color).next();

        for (int i = start - 90; i <= end - 90; i ++) {
            double angle = Math.toRadians(i);
            float x = (float)(Math.cos(angle) * radius) + cX;
            float y = (float)(Math.sin(angle) * radius) + cY;
            buf.vertex(mat, x, y, 0).color(color).next();
        }

        setupRender();
        drawBuffer(buf);
        endRender();
    }

    public static void fillCircle(DrawContext context, int cX, int cY, int radius, int color) {
        fillArc(context, cX, cY, radius, 0, 360, color);
    }

    public static void fillRoundRect(DrawContext context, int x, int y, int w, int h, int borderRadius, int color) {
        // corners [ top-left, top-right, bottom-right, bottom-left ]
        fillArc(context, x + borderRadius, y + borderRadius, borderRadius, 270, 360, color);
        fillArc(context, x + w - borderRadius, y + borderRadius, borderRadius, 0, 90, color);
        fillArc(context, x + w - borderRadius, y + h - borderRadius, borderRadius, 90, 180, color);
        fillArc(context, x + borderRadius, y + h - borderRadius, borderRadius, 180, 270, color);
        // filling [ middle, left-wing, right-wing ]
        fillRect(context, x + borderRadius, y, w - borderRadius - borderRadius, h, color);
        fillRect(context, x, y + borderRadius, borderRadius, h - borderRadius - borderRadius, color);
        fillRect(context, x + w - borderRadius, y + borderRadius, borderRadius, h - borderRadius - borderRadius, color);
    }

    public static void fillRoundTabTop(DrawContext context, int x, int y, int w, int h, int borderRadius, int color) {
        fillArc(context, x + borderRadius, y + borderRadius, borderRadius, 270, 360, color);
        fillArc(context, x + w - borderRadius, y + borderRadius, borderRadius, 0, 90, color);

        fillRect(context, x + borderRadius, y, w - borderRadius - borderRadius, h, color);
        fillRect(context, x, y + borderRadius, borderRadius, h - borderRadius, color);
        fillRect(context, x + w - borderRadius, y + borderRadius, borderRadius, h - borderRadius, color);
    }

    public static void fillRoundTabBottom(DrawContext context, int x, int y, int w, int h, int borderRadius, int color) {
        fillArc(context, x + w - borderRadius, y + h - borderRadius, borderRadius, 90, 180, color);
        fillArc(context, x + borderRadius, y + h - borderRadius, borderRadius, 180, 270, color);

        fillRect(context, x + borderRadius, y, w - borderRadius - borderRadius, h, color);
        fillRect(context, x, y, borderRadius, h - borderRadius, color);
        fillRect(context, x + w - borderRadius, y, borderRadius, h - borderRadius, color);
    }

    public static void fillRoundHoriLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        int radius = thickness / 2;
        fillArc(context, x + radius, y + radius, radius, 180, 360, color);
        fillArc(context, x + length - radius, y + radius, radius, 0, 180, color);
        fillRect(context, x + radius, y, length - thickness, thickness, color);
    }

    public static void fillRoundVertLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        int radius = thickness / 2;
        fillArc(context, x + radius, y + radius, radius, 270, 450, color);
        fillArc(context, x + radius, y + length - radius, radius, 90, 270, color);
        fillRect(context, x, y + radius, thickness, length - thickness, color);
    }
    
    // draw

    public static void drawRect(DrawContext context, int x, int y, int w, int h, int color) {
        drawHorLine(context, x, y, w, color);
        drawVerLine(context, x, y + 1, h - 2, color);
        drawVerLine(context, x + w - 1, y + 1, h - 2, color);
        drawHorLine(context, x, y + h - 1, w, color);
    }

    public static void drawHorLine(DrawContext context, int x, int y, int length, int color) {
        fillRect(context, x, y, length, 1, color);
    }

    public static void drawVerLine(DrawContext context, int x, int y, int length, int color) {
        fillRect(context, x, y, 1, length, color);
    }

    public static void drawLine(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        BufferBuilder buf = getBuffer();
        Matrix4f mat = context.getMatrices().peek().getPositionMatrix();

        buf.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buf.vertex(mat, (float)x1, (float)y1, 0).color(color).next();
        buf.vertex(mat, (float)x2, (float)y2, 0).color(color).next();

        setupRender();
        drawBuffer(buf);
        endRender();
    }

    public static void drawArc(DrawContext context, int cX, int cY, int radius, int start, int end, int color) {
        BufferBuilder buf = getBuffer();
        Matrix4f mat = context.getMatrices().peek().getPositionMatrix();

        buf.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        for (int i = start - 90; i <= end - 90; i++) {
            double angle = Math.toRadians(i);
            float x = (float)(Math.cos(angle) * radius) + cX;
            float y = (float)(Math.sin(angle) * radius) + cY;
            buf.vertex(mat, x, y, 0).color(color).next();
        }

        setupRender();
        drawBuffer(buf);
        endRender();
    }

    public static void drawCircle(DrawContext context, int cX, int cY, int radius, int color) {
        drawArc(context, cX, cY, radius, 0, 360, color);
    }

    public static void drawRoundRect(DrawContext context, int x, int y, int w, int h, int borderRadius, int color) {
        drawArc(context, x + borderRadius, y + borderRadius, borderRadius, 270, 360, color);
        drawArc(context, x + w - borderRadius, y + borderRadius, borderRadius, 0, 90, color);
        drawArc(context, x + w - borderRadius, y + h - borderRadius, borderRadius, 90, 180, color);
        drawArc(context, x + borderRadius, y + h - borderRadius, borderRadius, 180, 270, color);

        drawLine(context, x, y + borderRadius, x, y + h - borderRadius, color);
        drawLine(context, x + w, y + borderRadius, x + w, y + h - borderRadius, color);
        drawLine(context, x + borderRadius, y, x + w - borderRadius, y, color);
        drawLine(context, x + borderRadius, y + h, x + w - borderRadius, y + h, color);
    }

    public static void drawRoundHoriLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        int radius = thickness / 2;
        drawArc(context, x + radius, y + radius, radius, 180, 360, color);
        drawArc(context, x + length - radius, y + radius, radius, 0, 180, color);
        drawLine(context, x + radius, y, x + length - radius, y, color);
        drawLine(context, x + radius, y + thickness, x + length - radius, y + thickness, color);
    }

    public static void drawRoundVertLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        int radius = thickness / 2;
        drawArc(context, x + radius, y + radius, radius, 270, 450, color);
        drawArc(context, x + radius, y + length - radius, radius, 90, 270, color);
        drawLine(context, x, y + radius, x, y + length - radius, color);
        drawLine(context, x + thickness, y + radius, x + thickness, y + length - radius, color);
    }

    // default text

    public static void drawDefaultScaledText(DrawContext context, Text text, int x, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        x = (int)(x * rescale);
        y = (int)(y * rescale);

        drawDefaultText(context, text, x, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultCenteredScaledText(DrawContext context, Text text, int centerX, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        centerX = (int)(centerX * rescale);
        centerX = centerX - (mc.textRenderer.getWidth(text) / 2);
        y = (int)(y * rescale);

        drawDefaultText(context, text, centerX, y, shadow, color);
        m.scale(rescale, rescale, rescale);
    }

    public static void drawDefaultRightScaledText(DrawContext context, Text text, int rightX, int y, float scale, boolean shadow, int color) {
        MatrixStack m = context.getMatrices();
        m.scale(scale, scale, scale);

        float rescale = 1 / scale;
        rightX = (int)(rightX * rescale);
        rightX = rightX - mc.textRenderer.getWidth(text);
        y = (int)(y * rescale);

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

    // draw right-aligned text

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

    // misc

    public static void drawTexture(DrawContext context, Identifier texture, int x, int y, int w, int h) {
        context.drawTexture(texture, x, y, 0, 0, w, h, w, h);
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, float scale) {
        x = (int)(x / scale);
        y = (int)(y / scale);
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        context.drawItem(item, x, y);
        context.drawItemInSlot(mc.textRenderer, item, x, y);
        context.getMatrices().pop();
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, float scale, String text) {
        x = (int)(x / scale);
        y = (int)(y / scale);
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

    // util

    public static void setupRender() {
        disableCull();
        enableBlend();
        defaultBlendFunc();
        setShader(GameRenderer::getPositionColorProgram);
        setShaderColor(1, 1, 1, 1);
    }

    public static void endRender() {
        enableCull();
        disableBlend();
        setShader(GameRenderer::getPositionTexProgram);
    }

    public static void check(boolean check, String msg) {
        if (!check) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void drawBuffer(BufferBuilder buf) {
        BufferRenderer.drawWithGlobalProgram(buf.end());
    }

    public static BufferBuilder getBuffer() {
        return Tessellator.getInstance().getBuffer();
    }

    public static int width() {
        return mc.getWindow().getScaledWidth();
    }

    public static int height() {
        return mc.getWindow().getScaledHeight();
    }
}
