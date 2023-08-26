package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.Positionable;
import io.github.itzispyder.clickcrystals.gui.screens.HudEditScreen;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

@Environment(EnvType.CLIENT)
public abstract class Hud implements HudRenderCallback, Positionable, Global {

    private static final int DEFAULT_ARGB = 0x4007CDDF;
    private final Dimension defaultDimension;
    private int x, y, width, height, argb;
    private boolean renderBorder;

    public Hud(int x, int y, int width, int height, Dimension defaultDimension, int argb, boolean renderBorder) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.defaultDimension = defaultDimension;
        this.argb = argb;
        this.renderBorder = renderBorder;
    }

    public Hud(int x, int y, int width, int height) {
        this(x, y, width, height, new Dimension(x, y, width, height), DEFAULT_ARGB, true);
    }

    public Hud(Dimension dim) {
        this(dim.x, dim.y, dim.width, dim.height);
    }

    public Hud(Dimension dim, Dimension def) {
        this(dim.x, dim.y, dim.width, dim.height, def, DEFAULT_ARGB, true);
    }

    public Hud() {
        this(new Dimension());
    }

    public abstract void render(DrawContext context);

    public void renderBackdrop(DrawContext context) {
        RenderUtils.fill(context, getX(), getY(), getWidth(), getHeight(), getArgb());

        if (renderBorder) {
            RenderUtils.drawBorder(context, getX(), getY(), getWidth(), getHeight(), 0x40FFFFFF);
        }
    }

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        if (!GuiScreen.matchCurrent(HudEditScreen.class)) {
            render(context);
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Window getWindow() {
        return mc.getWindow();
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public int getArgb() {
        return argb;
    }

    public void setArgb(int argb) {
        this.argb = argb;
    }

    public void setRenderBorder(boolean renderBorder) {
        this.renderBorder = renderBorder;
    }

    public boolean canRenderBorder() {
        return renderBorder;
    }

    public Dimension getDefaultDimension() {
        return defaultDimension;
    }

    public void revertDimensions() {
        setDimensions(defaultDimension);
    }
}
