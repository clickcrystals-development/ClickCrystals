package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;

public class NoExplosion extends Module {

    public NoExplosion() {
        super("no-explosions", Categories.RENDERING, "Removes explosion particles.");
    }

    public static boolean hideExplosions = false;

    @Override
    protected void onEnable() {
        hideExplosions = true;
    }

    @Override
    protected void onDisable() {
        hideExplosions = false;
    }
}
