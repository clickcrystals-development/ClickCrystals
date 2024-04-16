package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;

public class AxeSwap extends Module implements Listener {

    public AxeSwap() {
        super("axe-swap", Categories.PVP, "Switch to axe if hitting a shielding opponent with a sword");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        if (e.getEntity() instanceof PlayerEntity p && p.isBlocking()) {
            if (HotbarUtils.nameContains("sword") && HotbarUtils.has(item -> item.getItem() instanceof AxeItem)) {
                HotbarUtils.search(item -> item.getItem() instanceof AxeItem);
            }
        }
    }
}
