package io.github.itzispyder.clickcrystals.gui.elements.design;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.elements.PressAction;
import io.github.itzispyder.clickcrystals.gui.elements.RenderAction;
import net.minecraft.client.gui.DrawContext;

public class AbstractElement extends GuiElement {

    private final RenderAction<AbstractElement> onRender;
    private final PressAction<AbstractElement> onPress;

    public AbstractElement(int x, int y, int width, int height, RenderAction<AbstractElement> onRender, PressAction<AbstractElement> pressAction) {
        super(x, y, width, height);
        this.onRender = onRender;
        this.onPress = pressAction;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.onRender.onRender(context, mouseX, mouseY, this);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.onPress.onPress(this);
    }

    public RenderAction<AbstractElement> getRenderAction() {
        return onRender;
    }

    public PressAction<AbstractElement> getPressAction() {
        return onPress;
    }
}
