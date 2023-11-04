package io.github.itzispyder.clickcrystals.gui_beta.misc.callbacks;

@FunctionalInterface
public interface MouseScrollCallback {

    void handleMouse(double mouseX, double mouseY, double amount);
}
