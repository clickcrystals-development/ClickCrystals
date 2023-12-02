package io.github.itzispyder.clickcrystals.gui.misc.callbacks;

@FunctionalInterface
public interface MouseScrollCallback {

    void handleMouse(double mouseX, double mouseY, double amount);
}
