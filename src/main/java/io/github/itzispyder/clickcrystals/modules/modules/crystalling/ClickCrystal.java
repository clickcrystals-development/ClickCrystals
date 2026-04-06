package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.BlockUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.item.Items;

@ModrinthNoNo
public class ClickCrystal extends Module implements Listener {

    public ClickCrystal() {
        super("click-crystal", Categories.CRYSTAL,"Binds end crystal place to left click");
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
        if (e.getPacket() instanceof ServerboundPlayerActionPacket packet) {
            if (packet.getAction() != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) return;
            final BlockPos pos = packet.getPos();
            if (!BlockUtils.canCrystalOn(pos)) return;

            if (HotbarUtils.isHolding(Items.END_CRYSTAL)) {
                e.setCancelled(true);
                BlockUtils.interact(pos,packet.getDirection());
            }
        }
    }
}
