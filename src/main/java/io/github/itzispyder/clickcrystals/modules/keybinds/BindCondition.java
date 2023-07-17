package io.github.itzispyder.clickcrystals.modules.keybinds;

import net.minecraft.client.gui.screen.Screen;

@FunctionalInterface
public interface BindCondition {

    boolean meets(Keybind bind, Screen screen);
}
