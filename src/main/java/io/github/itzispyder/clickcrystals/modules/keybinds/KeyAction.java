package io.github.itzispyder.clickcrystals.modules.keybinds;

import java.io.Serializable;

@FunctionalInterface
public interface KeyAction extends Serializable {

    void onKey(Keybind bind);
}
