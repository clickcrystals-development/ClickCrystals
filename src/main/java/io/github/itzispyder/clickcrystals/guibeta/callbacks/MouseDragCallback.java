package io.github.itzispyder.clickcrystals.guibeta.callbacks;

@FunctionalInterface
public interface MouseDragCallback {

    void handleMouse(double mouseX, double mouseY, int button, double deltaX, double deltaY);
}
