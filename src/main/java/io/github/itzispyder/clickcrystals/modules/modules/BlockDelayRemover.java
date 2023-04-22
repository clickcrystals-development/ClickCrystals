package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PlayerAttackBlockCooldownEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;

/**
 * BlockDelayRemover module
 */
public class BlockDelayRemover extends Module implements Listener {

    /**
     * Module constructor
     */
    public BlockDelayRemover() {
        super("BlockDelayRemover", Categories.OTHER,"Removes the delay between breaking a block and starting to break the next block. (Some AntiCheats don't like this module)");
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
