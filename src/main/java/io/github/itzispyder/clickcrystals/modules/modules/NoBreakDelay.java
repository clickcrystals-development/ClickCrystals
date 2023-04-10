package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PlayerAttackBlockCooldownEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;

/**
 * NoBreakDelay module
 */
public class NoBreakDelay extends Module implements Listener {

    /**
     * Module constructor
     */
    public NoBreakDelay() {
        super("NoBreakDelay", Categories.DETECTABLE,"Removes your block breaking cooldown.");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    /**
     * When a player has a break cooldown
     * @param e break cooldown event
     */
    @EventHandler
    private void onCooldown(PlayerAttackBlockCooldownEvent e) {
        if (!super.isEnabled()) return;
        e.setCooldown(0);
    }
}
