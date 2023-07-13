package io.github.itzispyder.clickcrystals.gui.elements.design;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.callbacks.ScreenRenderCallback;
import net.minecraft.client.gui.DrawContext;

public class AbstractElement extends GuiElement {

    private final ScreenRenderCallback onRender;

    public AbstractElement(int x, int y, int width, int height, ScreenRenderCallback onRender) {
        super(x, y, width, height);
        this.onRender = onRender;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.onRender.handleScreen(context, mouseX, mouseY, 0F);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public ScreenRenderCallback getRenderCallback() {
        return onRender;
    }
}
