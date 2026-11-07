package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.BlockUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;

/**
 * One deliberate left click on a visible valid crystal base becomes one
 * ordinary block interaction. Never redirects an attack to a stale position.
 */
@ModrinthNoNo
public class ClickCrystal extends Module implements Listener {
    public ClickCrystal() {
        super("click-crystal", Categories.CRYSTAL, "Place a crystal with a deliberate left click on its base");
    }

    @Override
    protected void onEnable() { system.addListener(this); }

    @Override
    protected void onDisable() { system.removeListener(this); }

    @EventHandler
    private void onSendPacket(PacketSendEvent event) {
        if (!(event.getPacket() instanceof PlayerActionC2SPacket packet)) return;
        if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;
        if (PlayerUtils.invalid() || mc.currentScreen != null) return;
        if (!HotbarUtils.isHolding(Items.END_CRYSTAL)) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) return;
        if (!hit.getBlockPos().equals(packet.getPos())) return;
        if (!BlockUtils.canCrystalOn(hit.getBlockPos())) return;

        event.setCancelled(true);
        BlockUtils.interact(hit);
    }
}
