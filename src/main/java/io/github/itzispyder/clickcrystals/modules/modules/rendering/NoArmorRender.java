package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;

public class NoArmorRender extends Module {

    public boolean armor = true; // Add this line to control the armor option

    public NoArmorRender() {
        super("No-Armor-Render", Categories.RENDER, "Make armor invisible,try to use the enemy hud with this module!");
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}