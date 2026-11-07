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
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;

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
        if (!(event.getPacket() instanceof ServerboundPlayerActionPacket packet)) return;
        if (packet.getAction() != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) return;
        if (PlayerUtils.invalid() || mc.gui.screen() != null) return;
        if (!HotbarUtils.isHolding(Items.END_CRYSTAL)) return;
        if (!(mc.hitResult instanceof BlockHitResult hit)) return;
        if (!hit.getBlockPos().equals(packet.getPos())) return;
        if (!BlockUtils.canCrystalOn(hit.getBlockPos())) return;

        event.setCancelled(true);
        BlockUtils.interact(hit);
    }
}
