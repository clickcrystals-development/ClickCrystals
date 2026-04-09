package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public class ClientCryst extends Module implements Listener {

    public ClientCryst() {
        super("client-crystals", Categories.CRYSTAL, "Removes crystals client-side the moment you punch them");
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
    private void onPacketSend(PlayerAttackEntityEvent e) {
        final Entity ent = e.getEntity();

        if (ent == null) return;
        if (PlayerUtils.invalid()) return;
        if (mc.isLocalServer()) return;
        if (!InteractionUtils.canBreakCrystals()) return;

        if (ent instanceof EndCrystal crystal) {
            crystal.remove(Entity.RemovalReason.KILLED);
            crystal.onClientRemoval();
        }
    }
}
