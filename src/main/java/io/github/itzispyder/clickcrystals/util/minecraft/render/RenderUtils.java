package io.github.itzispyder.clickcrystals.util.minecraft.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.states.ClickCrystalsQuadState;
import io.github.itzispyder.clickcrystals.util.minecraft.render.states.ClickCrystalsRoundRectState;
import io.github.itzispyder.clickcrystals.util.minecraft.render.states.ClickCrystalsRoundRectWireframeState;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;

public final class RenderUtils implements Global {

    // fill

    public static void fillRect(DrawContext context, int x, int y, int w, int h, int color) {
        context.state.addSimpleElement(new ClickCrystalsQuadState(context, x, y, w, h, color));
    }

    public static void fillVerticalGradient(DrawContext context, int x, int y, int w, int h, int colorTop, int colorBottom) {
        context.state.addSimpleElement(new ClickCrystalsQuadState(context, x, y, w, h, colorTop, colorTop, colorBottom, colorBottom));
    }

    public static void fillCircle(DrawContext context, int cX, int cY, int radius, int color) {
        fillRoundRect(context, cX - radius, cY - radius, radius * 2, radius * 2, radius, color);
    }

    public static void fillRoundRect(DrawContext context, int x, int y, int w, int h, int r, int color) {
        context.state.addSimpleElement(new ClickCrystalsRoundRectState(context, x, y, w, h, r, color));
    }

    public static void fillRoundRectGradient(DrawContext context, int x, int y, int w, int h, int r, int color1, int color2, int color3, int color4, int colorCenter) {
        context.state.addSimpleElement(new ClickCrystalsRoundRectState(context, x, y, w, h, r, color1, color2, color3, color4, colorCenter));
    }

    public static void fillRoundShadow(DrawContext context, int x, int y, int w, int h, int r, float thickness, int innerColor, int outerColor) {
        fillRoundShadowGradient(context, x, y, w, h, r, thickness, innerColor, outerColor, innerColor, outerColor, innerColor, outerColor, innerColor, outerColor);
    }

    public static void fillRoundShadowGradient(DrawContext context, int x, int y, int w, int h, int r, float thickness, int inner1, int outer1, int inner2, int outer2, int inner3, int outer3, int inner4, int outer4) {
        context.state.addSimpleElement(new ClickCrystalsRoundRectWireframeState(context, x, y, w, h, r, thickness, inner1, outer1, inner2, outer2, inner3, outer3, inner4, outer4));
    }

    public static void fillRoundShadow(DrawContext context, int x, int y, int w, int h, int r, float thickness, int color) {
        fillRoundShadow(context, x, y, w, h, r, thickness, color, new Color(color).getHexCustomAlpha(0.0));
    }

    public static void fillRoundTabTop(DrawContext context, int x, int y, int w, int h, int r, int color) {
        BufferBuilder buf = getBuffer(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        Matrix3x2f mat = context.getMatrices();

        buf.vertex(mat, x + w / 2F, y + h / 2F, 0).color(color);

        int[][] corners = {
                { x + r, y + r },
                { x + w - r, y + r }
        };

        for (int corner = 0; corner < 2; corner++) {
            int cornerStart = (corner - 2) * 90;
            int cornerEnd = cornerStart + 90;
            for (int i = cornerStart; i <= cornerEnd; i += 10) {
                float angle = (float)Math.toRadians(i);
                float rx = corners[corner][0] + (float)(Math.cos(angle) * r);
                float ry = corners[corner][1] + (float)(Math.sin(angle) * r);
                buf.vertex(mat, rx, ry, 0).color(color);
            }
        }

        buf.vertex(mat, x + w, y + h, 0).color(color);
        buf.vertex(mat, x, y + h, 0).color(color);
        buf.vertex(mat, x, corners[0][1], 0).color(color); // connect last to first vertex

        drawBuffer(buf, ClickCrystalsRenderLayers.TRI_FAN);
    }

    public static void fillRoundTabBottom(DrawContext context, int x, int y, int w, int h, int r, int color) {
        BufferBuilder buf = getBuffer(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        Matrix3x2f mat = context.getMatrices();

        buf.vertex(mat, x + w / 2F, y + h / 2F, 0).color(color);

        int[][] corners = {
                { x + w - r, y + h - r},
                { x + r, y + h - r }
        };

        for (int corner = 0; corner < 2; corner++) {
            int cornerStart = corner * 90;
            int cornerEnd = cornerStart + 90;
            for (int i = cornerStart; i <= cornerEnd; i += 10) {
                float angle = (float)Math.toRadians(i);
                float rx = corners[corner][0] + (float)(Math.cos(angle) * r);
                float ry = corners[corner][1] + (float)(Math.sin(angle) * r);
                buf.vertex(mat, rx, ry, 0).color(color);
            }
        }

        buf.vertex(mat, x, y, 0).color(color);
        buf.vertex(mat, x + w, y, 0).color(color);
        buf.vertex(mat, x + w, corners[0][1], 0).color(color); // connect last to first vertex

        drawBuffer(buf, ClickCrystalsRenderLayers.TRI_FAN);
    }

    public static void fillRoundHoriLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        fillRoundRect(context, x, y, length, thickness, thickness / 2, color);
    }

    public static void fillRoundVertLine(DrawContext context, int x, int y, int length, int thickness, int color) {
        fillRoundRect(context, x, y, thickness, length, thickness / 2, color);
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

    public static void drawRoundRect(DrawContext context, int x, int y, int w, int h, int r, int color) {
        fillRoundShadow(context, x, y, w, h, r, 0.5F, color);
    }

    // default text

    public static void drawDefaultScaledText(DrawContext context, Text text, int x, int y, float scale, boolean shadow, int color) {
        Matrix3x2fStack m = context.getMatrices().pushMatrix();
        m.scale(scale);

        float rescale = 1 / scale;
        x = (int)(x * rescale);
        y = (int)(y * rescale);

        drawDefaultText(context, text, x, y, shadow, color);
        context.getMatrices().popMatrix();
    }

    public static void drawDefaultCenteredScaledText(DrawContext context, Text text, int centerX, int y, float scale, boolean shadow, int color) {
        Matrix3x2fStack m = context.getMatrices().pushMatrix();
        m.scale(scale);

        float rescale = 1 / scale;
        centerX = (int)(centerX * rescale);
        centerX = centerX - (mc.textRenderer.getWidth(text) / 2);
        y = (int)(y * rescale);

        drawDefaultText(context, text, centerX, y, shadow, color);
        context.getMatrices().popMatrix();
    }

    public static void drawDefaultRightScaledText(DrawContext context, Text text, int rightX, int y, float scale, boolean shadow, int color) {
        Matrix3x2fStack m = context.getMatrices().pushMatrix();
        m.scale(scale);

        float rescale = 1 / scale;
        rightX = (int)(rightX * rescale);
        rightX = rightX - mc.textRenderer.getWidth(text);
        y = (int)(y * rescale);

        drawDefaultText(context, text, rightX, y, shadow, color);
        context.getMatrices().popMatrix();
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
        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, w, h, w, h);
    }

    public static void drawRoundTexture(DrawContext context, Identifier texture, int x, int y, int w, int h, int r) {
        r = MathUtils.clamp(r, 0, Math.min(w, h) / 2);

        BufferBuilder buf = getBuffer(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_TEXTURE_COLOR);
        Matrix3x2f mat = context.getMatrices();

        buf.vertex(mat, x + w / 2F, y + h / 2F, 0).texture(0.5F, 0.5F).color(-1);

        int[][] corners = {
                { x + w - r, y + r },
                { x + w - r, y + h - r},
                { x + r, y + h - r },
                { x + r, y + r }
        };

        for (int corner = 0; corner < 4; corner++) {
            int cornerStart = (corner - 1) * 90;
            int cornerEnd = cornerStart + 90;
            for (int i = cornerStart; i <= cornerEnd; i += 10) {
                float angle = (float)Math.toRadians(i);
                float rx = corners[corner][0] + (float)(Math.cos(angle) * r);
                float ry = corners[corner][1] + (float)(Math.sin(angle) * r);
                float u = (rx - x) / w;
                float v = (ry - y) / h;
                buf.vertex(mat, rx, ry, 0).texture(u, v).color(-1);
            }
        }

        buf.vertex(mat, corners[0][0], y, 0).texture(((float)corners[0][0] - x) / w, 0).color(-1); // connect last to first vertex

        drawBuffer(buf, ClickCrystalsRenderLayers.TEX_TRI_FAN.apply(texture));
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, float scale) {
        x = (int)(x / scale);
        y = (int)(y / scale);
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale);
        context.drawItem(item, x, y);
        context.drawStackOverlay(mc.textRenderer, item, x, y);
        context.getMatrices().popMatrix();
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, float scale, String text) {
        x = (int)(x / scale);
        y = (int)(y / scale);
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale);
        context.drawItem(item, x, y);
        context.drawStackOverlay(mc.textRenderer, item, x, y, text);
        context.getMatrices().popMatrix();
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y, int size) {
        drawItem(context, item, x, y, size / 16.0F);
    }

    public static void drawItem(DrawContext context, ItemStack item, int x, int y) {
        drawItem(context, item, x, y, 1.0F);
    }

    // util
    public static void drawBuffer(BufferBuilder buf, RenderLayer layer) {
        layer.draw(buf.end());
    }

    public static BufferBuilder getBuffer(VertexFormat.DrawMode drawMode, VertexFormat format) {
        return Tessellator.getInstance().begin(drawMode, format);
    }

    public static int width() {
        return mc.getWindow().getScaledWidth();
    }

    public static int height() {
        return mc.getWindow().getScaledHeight();
    }
}