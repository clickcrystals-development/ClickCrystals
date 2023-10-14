package io.github.itzispyder.clickcrystals.gui_beta.misc.brushes;

import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class RoundRectBrush {

    public static void drawRoundRect(DrawContext context, int x, int y, int w, int h, int borderRadius, Gray gray) {
        // corners
        Identifier circle = Tex.Shapes.getCircle(gray);
        int diameter = borderRadius * 2;
        RenderUtils.drawTexture(context, circle, x, y, diameter, diameter); // top-left
        RenderUtils.drawTexture(context, circle, x + w - diameter, y, diameter, diameter); // top-right
        RenderUtils.drawTexture(context, circle, x + w - diameter, y + h - diameter, diameter, diameter); // bottom-right
        RenderUtils.drawTexture(context, circle, x, y + h - diameter, diameter, diameter); // bottom left

        // fillings
        int color = gray.argb;
        RenderUtils.fill(context, x + borderRadius, y, w - diameter, h, color); // middle
        RenderUtils.fill(context, x, y + borderRadius, borderRadius, h - diameter, color); // left wing
        RenderUtils.fill(context, x + w - borderRadius, y + borderRadius, borderRadius, h - diameter, color); // right wing
    }

    public static void drawRoundHoriLine(DrawContext context, int x, int y, int length, int thickness, Gray gray) {
        // thickness = diameter
        // ends
        Identifier circle = Tex.Shapes.getCircle(gray);
        RenderUtils.drawTexture(context, circle, x, y, thickness, thickness);
        RenderUtils.drawTexture(context, circle, x + length - thickness, y, thickness, thickness);

        // fillings
        int color = gray.argb;
        int diameter = thickness / 2;
        RenderUtils.fill(context, x + diameter, y, length - thickness, thickness, color);
    }

    public static void drawRoundVertLine(DrawContext context, int x, int y, int length, int thickness, Gray gray) {
        // thickness = diameter
        // ends
        Identifier circle = Tex.Shapes.getCircle(gray);
        RenderUtils.drawTexture(context, circle, x, y, thickness, thickness);
        RenderUtils.drawTexture(context, circle, x, y + length - thickness, thickness, thickness);

        // fillings
        int color = gray.argb;
        int diameter = thickness / 2;
        RenderUtils.fill(context, x, y + diameter, thickness, length - thickness, color);
    }

    public static void drawTabTop(DrawContext context, int x, int y, int w, int h, int borderRadius, Gray gray) {
        // corners
        Identifier circle = Tex.Shapes.getCircle(gray);
        int diameter = borderRadius * 2;
        RenderUtils.drawTexture(context, circle, x, y, diameter, diameter); // top-left
        RenderUtils.drawTexture(context, circle, x + w - diameter, y, diameter, diameter); // top-right

        // fillings
        int color = gray.argb;
        RenderUtils.fill(context, x + borderRadius, y, w - diameter, h, color); // middle
        RenderUtils.fill(context, x, y + borderRadius, borderRadius, h - borderRadius, color); // left wing
        RenderUtils.fill(context, x + w - borderRadius, y + borderRadius, borderRadius, h - borderRadius, color); // right wing
    }

    public static void drawTabBottom(DrawContext context, int x, int y, int w, int h, int borderRadius, Gray gray) {
        // corners
        Identifier circle = Tex.Shapes.getCircle(gray);
        int diameter = borderRadius * 2;
        RenderUtils.drawTexture(context, circle, x + w - diameter, y + h - diameter, diameter, diameter); // bottom-right
        RenderUtils.drawTexture(context, circle, x, y + h - diameter, diameter, diameter); // bottom left

        // fillings
        int color = gray.argb;
        RenderUtils.fill(context, x + borderRadius, y, w - diameter, h, color); // middle
        RenderUtils.fill(context, x, y, borderRadius, h - borderRadius, color); // left wing
        RenderUtils.fill(context, x + w - borderRadius, y, borderRadius, h - borderRadius, color); // right wing
    }
}
