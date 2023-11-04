package io.github.itzispyder.clickcrystals.gui_beta.misc.callbacks;

import io.github.itzispyder.clickcrystals.gui_beta.ClickType;

@FunctionalInterface
public interface KeyPressCallback {

    void handleKey(int key, ClickType click, int scancode, int modifiers);
}
