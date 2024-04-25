package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;

public class ArrayListHud extends DummyModule {
    public ArrayListHud() {
        super("array-list", Categories.MISC, "Shows the enabled modules on screen");
    }
}
///TODO make colors to animate, add settings to color, add keybinds to modules in the ModuleListTextHud class and add a way to choose hud direction (up or down), make it so the defualt colors will be cc colors, fix armor hud to "Tail"