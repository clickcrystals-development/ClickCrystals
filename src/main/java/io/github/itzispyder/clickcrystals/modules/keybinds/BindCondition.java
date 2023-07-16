package io.github.itzispyder.clickcrystals.modules.keybinds;

import net.minecraft.client.gui.screen.Screen;

import java.io.Serializable;

@FunctionalInterface
public interface BindCondition extends Serializable {

    boolean meets(Keybind bind, Screen screen);
}
