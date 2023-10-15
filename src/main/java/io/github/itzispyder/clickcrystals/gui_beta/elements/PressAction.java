package io.github.itzispyder.clickcrystals.gui_beta.elements;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;

@FunctionalInterface
public interface PressAction<T extends GuiElement> {

    void onPress(T button);
}
