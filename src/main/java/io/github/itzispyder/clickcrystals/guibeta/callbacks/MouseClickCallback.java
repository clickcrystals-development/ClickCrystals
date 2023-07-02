package io.github.itzispyder.clickcrystals.guibeta.callbacks;

import io.github.itzispyder.clickcrystals.guibeta.ClickType;

@FunctionalInterface
public interface MouseClickCallback {

    void handleMouse(double mouseX, double mouseY, int button, ClickType click);
}
