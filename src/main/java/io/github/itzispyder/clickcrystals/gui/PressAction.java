package io.github.itzispyder.clickcrystals.gui;

@FunctionalInterface
public interface PressAction<T extends GuiElement> {

    void onPress(T button);
}
