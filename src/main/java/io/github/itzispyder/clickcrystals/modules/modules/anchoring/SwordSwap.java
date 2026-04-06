package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;

@ModrinthNoNo
public class SwordSwap extends Module implements Listener {

    public SwordSwap() {
        super("sword-swap", Categories.PVP, "Switch to sword after hitting a shielding opponent with an axe");
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
        if (e.getEntity() instanceof Player p && !p.isBlocking() && isHoldingShield(p)) {
            if (HotbarUtils.nameContains("axe") && HotbarUtils.has(item -> item.getItem().getDescriptionId().contains("sword"))) {
                HotbarUtils.search(item -> item.getItem().getDescriptionId().contains("sword"));
            }
        }
    }

    private boolean isHoldingShield(Player p) {
        boolean mainhand = p.getMainHandItem() != null && p.getMainHandItem().getItem() instanceof ShieldItem;
        boolean offhand = p.getOffhandItem() != null && p.getOffhandItem().getItem() instanceof ShieldItem;
        return mainhand || offhand;
    }
}
