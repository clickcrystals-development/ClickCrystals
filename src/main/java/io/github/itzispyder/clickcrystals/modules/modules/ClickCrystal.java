package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.InteractionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/**
 * ClickCrystal module
 */
public class ClickCrystal extends Module implements Listener {

    /**
     * Module constructor
     */
    public ClickCrystal() {
        super("ClickCrystal", Categories.CRYSTALLING,"Punch an obsidian block with an end crystal to place the end crystal.");
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
    private void onSendPacket(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;
            final BlockPos pos = packet.getPos();
            if (!BlockUtils.isCrystallabe(pos)) return;

            if (HotbarUtils.isHolding(Items.END_CRYSTAL)) {
                e.setCancelled(true);
                BlockUtils.interact(pos,packet.getDirection());
            }
        }
    }

    @EventHandler
    private void onClientTick(ClientTickEndEvent e) {
        if (mc.options.useKey.isPressed()) {
            HitResult hit = mc.crosshairTarget;
            if (hit == null) return;
            if (hit.getType() != HitResult.Type.ENTITY) return;
            Entity ent = ((EntityHitResult) hit).getEntity();
            if (!(ent instanceof EndCrystalEntity)) return;
            InteractionUtils.doAttack();
        }
    }
}
