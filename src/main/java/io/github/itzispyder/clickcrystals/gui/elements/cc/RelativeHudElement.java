package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.hud.Hud;
import net.minecraft.client.gui.DrawContext;

public class RelativeHudElement extends GuiElement {

    private final Hud hud;

    public RelativeHudElement(Hud hud) {
        super(hud.getX(), hud.getY(), hud.getWidth(), hud.getHeight());
        this.hud = hud;
        this.setDraggable(true);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        hud.setX(x);
        hud.setY(y);
        hud.render(context);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public Hud getHud() {
        return hud;
    }
}
