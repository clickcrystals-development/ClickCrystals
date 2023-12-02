package io.github.itzispyder.clickcrystals.gui.misc.callbacks;

import io.github.itzispyder.clickcrystals.gui.ClickType;

@FunctionalInterface
public interface KeyPressCallback {

    void handleKey(int key, ClickType click, int scancode, int modifiers);
}
