package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scheduler.ScheduledTask;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.Randomizer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

/**
 * GlowStoneSearch module
 */
public class GlowStoneSearch extends Module implements Listener {

    /**
     * Module constructor
     */
    public GlowStoneSearch() {
        super("GlowStoneSearch","Searches your hotbar for glowstone after clicking an anchor.");
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
     * Module function
     * @param e packet send event
     */
    @EventHandler
    private void onPacketSend(PacketSendEvent e) {
        if (!super.isEnabled()) return;
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            BlockState state = mc.player.getWorld().getBlockState(packet.getBlockHitResult().getBlockPos());
            if (state == null) return;
            if (!mc.options.useKey.isPressed()) return;

            if (state.isOf(Blocks.RESPAWN_ANCHOR)) {
                int charges = state.get(RespawnAnchorBlock.CHARGES); // charges of the clicked block
                if (HotbarUtils.isHolding(Items.GLOWSTONE) && charges > 0) { // after charging the anchor
                    new ScheduledTask(() -> {
                        HotbarUtils.search(Items.RESPAWN_ANCHOR);
                    }).runDelayedTask(Randomizer.rand(50));
                    return;
                }
            }

            if (HotbarUtils.isHolding(Items.RESPAWN_ANCHOR) && !state.isOf(Blocks.RESPAWN_ANCHOR)) {
                new ScheduledTask(() -> {
                    HotbarUtils.search(Items.GLOWSTONE);
                }).runDelayedTask(Randomizer.rand(50));
            }
        }
    }
}
