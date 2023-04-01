package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * ObsidianSearch module
 */
public class ObsidianSearch extends Module implements Listener {

    private long cooldown;

    public ObsidianSearch() {
        super("ObsidianSearch","Searches your hotbar for an obsidian when you right click a non-obsidian block with a crystal.");
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
    private void onPacketSend(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (!HotbarUtils.isHolding(Items.END_CRYSTAL)) return;
            if (cooldown > System.currentTimeMillis()) return;
            cooldown = System.currentTimeMillis() + (50 * 4);
            if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;
            BlockPos pos = packet.getPos();
            if (BlockUtils.matchBlock(pos,Blocks.OBSIDIAN) || BlockUtils.matchBlock(pos,Blocks.BEDROCK)) return;

            e.setCancelled(true);
            HotbarUtils.search(Items.OBSIDIAN);
            BlockUtils.interact(pos,packet.getDirection());
            HotbarUtils.search(Items.END_CRYSTAL);
        }
    }
}
