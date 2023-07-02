package io.github.itzispyder.clickcrystals.guibeta.callbacks;

@FunctionalInterface
public interface MouseScrollCallback {

    void handleMouse(double mouseX, double mouseY, double amount);
}
