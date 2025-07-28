package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.Positionable;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.gui.screens.HudEditScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

@Environment(EnvType.CLIENT)
public abstract class Hud implements Positionable, Global {

    public static final Color DEFAULT_COLOR = new Color(0x4007CDDF);
    private final Dimension defaultDimension;
    private int x, y, width, height, argb;
    private boolean fixed;
    private final String id;

    public Hud(String id, int x, int y, int width, int height, Dimension defaultDimension, int argb, boolean renderBorder) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.defaultDimension = defaultDimension;
        this.argb = argb;
        this.id = id;
        this.fixed = false;
    }

    public Hud(String id, int x, int y, int width, int height) {
        this(id, x, y, width, height, new Dimension(x, y, width, height), DEFAULT_COLOR.getHex(), true);
    }

    public Hud(String id, Dimension dim) {
        this(id, dim.x, dim.y, dim.width, dim.height);
    }

    public Hud(String id, Dimension dim, Dimension def) {
        this(id, dim.x, dim.y, dim.width, dim.height, def, DEFAULT_COLOR.getHex(), true);
    }

    public Hud(String id) {
        this(id, new Dimension());
    }

    public abstract void render(DrawContext context, float tickDelta);

    public void renderBackdrop(DrawContext context) {
        RenderUtils.fillRoundRect(context, getX(), getY(), getWidth(), getHeight(), 5, getArgb());
    }

    public boolean canRender() {
        return (Module.isEnabled(InGameHuds.class) || isFixed()) && !GuiScreen.matchCurrent(HudEditScreen.class);
    }

    public String getId() {
        return id;
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

    public Dimension getDefaultDimension() {
        return defaultDimension;
    }

    public void revertDimensions() {
        setDimensions(defaultDimension);
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isFixed() {
        return fixed;
    }
}
