package io.github.itzispyder.clickcrystals.util.minecraft.render;

import net.minecraft.client.gui.ScreenRect;

public final class GuiPixelBoundsTest {
    private static int checks;
    private static void equal(String description, ScreenRect actual, ScreenRect expected) {
        checks++;
        if (!actual.equals(expected)) throw new AssertionError(description + " expected " + expected + " actual " + actual);
    }
    public static void main(String[] args) {
        equal("integer", GuiPixelBounds.fromCorners(10, 20, 30, 40), new ScreenRect(10, 20, 20, 20));
        equal("subpixel", GuiPixelBounds.fromCorners(.2f, .2f, .7f, .7f), new ScreenRect(0, 0, 1, 1));
        equal("negative", GuiPixelBounds.fromCorners(-.2f, -1.9f, .2f, 2.1f), new ScreenRect(-1, -2, 2, 5));
        equal("reverse", GuiPixelBounds.fromCorners(5.9f, 8.2f, 1.1f, 3.8f), new ScreenRect(1, 3, 5, 6));
        equal("zero", GuiPixelBounds.fromCorners(5, 6, 5, 6), new ScreenRect(5, 6, 0, 0));
        equal("fractional", GuiPixelBounds.fromCorners(2.75f, 1.75f, 7.25f, 3.25f), new ScreenRect(2, 1, 6, 3));
        java.util.Random random = new java.util.Random(4251);
        for (int i = 0; i < 10_000; i++) {
            float x1 = (random.nextFloat() - .5f) * 512f, x2 = (random.nextFloat() - .5f) * 512f;
            float y1 = (random.nextFloat() - .5f) * 512f, y2 = (random.nextFloat() - .5f) * 512f;
            ScreenRect bounds = GuiPixelBounds.fromCorners(x1, y1, x2, y2);
            if (bounds.x() > Math.min(x1, x2) || bounds.y() > Math.min(y1, y2)
                    || bounds.x() + bounds.width() < Math.max(x1, x2)
                    || bounds.y() + bounds.height() < Math.max(y1, y2)
                    || bounds.width() < 0 || bounds.height() < 0)
                throw new AssertionError("under-covered sample " + i + ": " + bounds);
            checks++;
        }
        System.out.println("PASS: " + checks + " GUI bounds cases");
    }
}
