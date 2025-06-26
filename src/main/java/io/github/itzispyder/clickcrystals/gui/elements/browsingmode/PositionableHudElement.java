package io.github.itzispyder.clickcrystals.gui.elements.browsingmode;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import net.minecraft.client.gui.DrawContext;

public class PositionableHudElement extends GuiElement {

    private final Hud hud;

    public PositionableHudElement(Hud hud) {
        super(hud.getX(), hud.getY(), hud.getWidth(), hud.getHeight());
        this.hud = hud;
        this.setDraggable(true);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        hud.setX(x);
        hud.setY(y);
        hud.render(context, 1.0F);
        this.setHeight(hud.getHeight());
        this.setWidth(hud.getWidth());
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public Hud getHud() {
        return hud;
    }
}
