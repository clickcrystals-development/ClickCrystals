package io.github.itzispyder.clickcrystals.gui.elements.common;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface RenderAction<T extends GuiElement> {

    void onRender(GuiGraphics context, int mouseX, int mouseY, T button);
}
