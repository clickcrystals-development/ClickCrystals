package io.github.itzispyder.clickcrystals.modules.keybinds;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface BindCondition {

    boolean meets(Keybind bind, Screen screen);
}
