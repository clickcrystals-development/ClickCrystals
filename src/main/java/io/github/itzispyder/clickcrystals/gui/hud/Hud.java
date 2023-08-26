package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.Positionable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public abstract class Hud implements HudRenderCallback, Positionable, Global {

    private final Dimension defaultDimension;
    private int x, y, width, height;

    public Hud(int x, int y, int width, int height, Dimension defaultDimension) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.defaultDimension = defaultDimension;
    }

    public Hud(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.defaultDimension = new Dimension(x, y, width, height);
    }

    public Hud(Dimension dim) {
        this(dim.x, dim.y, dim.width, dim.height);
    }

    public Hud(Dimension dim, Dimension def) {
        this(dim.x, dim.y, dim.width, dim.height, def);
    }

    public Hud() {
        this(new Dimension());
    }

    public abstract void render(DrawContext context);

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        render(context);
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

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public Dimension getDefaultDimension() {
        return defaultDimension;
    }

    public void revertDimensions() {
        setDimensions(defaultDimension);
    }
}
