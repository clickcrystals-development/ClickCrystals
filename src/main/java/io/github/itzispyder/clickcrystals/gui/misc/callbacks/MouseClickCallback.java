package io.github.itzispyder.clickcrystals.gui.misc.callbacks;

import io.github.itzispyder.clickcrystals.gui.ClickType;

@FunctionalInterface
public interface MouseClickCallback {

    void handleMouse(double mouseX, double mouseY, int button, ClickType click);
}
