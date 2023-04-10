package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;

/**
 * AhhGameCrashed module
 */
public class AhhGameCrashed extends Module {

    public AhhGameCrashed() {
        super("AhhGameCrashed", Categories.MISC,"\"oops, my game crashed i swear! rematch?\"");
    }

    @Override
    protected void onEnable() {
        super.setEnabled(false);
        System.exit(-1);
    }

    @Override
    protected void onDisable() {

    }
}
