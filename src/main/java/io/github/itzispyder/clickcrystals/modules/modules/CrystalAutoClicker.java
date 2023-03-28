package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.modules.Module;

/**
 * CrystalAutoClicker module
 */
public class CrystalAutoClicker extends Module implements Listener {

    /**
     * Module constructor
     */
    public CrystalAutoClicker() {
        super("CrystalAutoClicker","This is an AUTOCLICKER and a MACRO that works with CLICKRYSTAL MODULE, do NOT use this on servers unless hacking is allowed!");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }
}
