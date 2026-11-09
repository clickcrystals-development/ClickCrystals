package io.github.itzispyder.clickcrystals.util.minecraft.render;

import net.minecraft.client.gui.navigation.ScreenRectangle;

/** Computes conservative bounds to avoid dropping subpixel GUI geometry. */
public final class GuiPixelBounds {
    private GuiPixelBounds() {}

    public static ScreenRectangle fromCorners(float x1, float y1, float x2, float y2) {
        int left = (int) Math.floor(Math.min(x1, x2));
        int top = (int) Math.floor(Math.min(y1, y2));
        int right = (int) Math.ceil(Math.max(x1, x2));
        int bottom = (int) Math.ceil(Math.max(y1, y2));
        return new ScreenRectangle(left, top, Math.max(0, right - left), Math.max(0, bottom - top));
    }
}
