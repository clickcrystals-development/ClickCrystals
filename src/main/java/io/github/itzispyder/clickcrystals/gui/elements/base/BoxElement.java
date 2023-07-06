package io.github.itzispyder.clickcrystals.gui.elements.base;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;

public class BoxElement extends GuiElement {

    private int color;

    public BoxElement(int x, int y, int width, int height, int color) {
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        DrawableUtils.fill(context, x, y, width, height, color);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
