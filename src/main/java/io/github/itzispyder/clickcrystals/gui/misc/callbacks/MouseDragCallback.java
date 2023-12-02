package io.github.itzispyder.clickcrystals.gui.misc.callbacks;

@FunctionalInterface
public interface MouseDragCallback {

    void handleMouse(double mouseX, double mouseY, int button, double deltaX, double deltaY);
}
