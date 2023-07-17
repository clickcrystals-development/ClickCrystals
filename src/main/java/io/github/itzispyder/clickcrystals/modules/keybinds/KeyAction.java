package io.github.itzispyder.clickcrystals.modules.keybinds;

@FunctionalInterface
public interface KeyAction {

    void onKey(Keybind bind);
}
