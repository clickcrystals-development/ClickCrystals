package io.github.itzispyder.clickcrystals.guibeta.callbacks;

import io.github.itzispyder.clickcrystals.guibeta.ClickType;

@FunctionalInterface
public interface KeyPressCallback {

    void handleKey(int key, ClickType click, int scancode, int modifiers);
}
