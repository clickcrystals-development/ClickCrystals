package io.github.itzispyder.clickcrystals.gui_beta.elements;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import net.minecraft.client.gui.DrawContext;

@FunctionalInterface
public interface RenderAction<T extends GuiElement> {

    void onRender(DrawContext context, int mouseX, int mouseY, T button);
}
