package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.design.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.hud.RelativeHud;
import net.minecraft.client.gui.DrawContext;

public class RelativeHudElement extends GuiElement {

    private final RelativeHud hud;

    public RelativeHudElement(RelativeHud hud) {
        super(hud.getX(), hud.getY(), hud.getWidth(), hud.getHeight());
        this.hud = hud;

        AbstractElement btnR = AbstractElement.create()
                .dimensions(10, 10)
                .pos(hud.getX() + hud.getWidth() - 10, hud.getY())
                .onRender((context, mouseX, mouseY, button) -> {
                    context.drawTexture(GuiTextures.RESET, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
                })
                .onPress(button -> {
                    hud.revertDimensions();
                })
                .build();

        this.addChild(btnR);
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

    public RelativeHud getHud() {
        return hud;
    }
}
