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
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

/**
 * Glow stone search module
 */
public class GlowStoneSearch extends Module implements Listener {

    public GlowStoneSearch() {
        super("GlowStoneSearch","Limits your glow stone charges in an anchor to one click.");
        system.addListener(this);
        super.enabled = true;
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
        if (!super.isEnabled()) return;
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            BlockState state = mc.player.getWorld().getBlockState(packet.getBlockHitResult().getBlockPos());

            if (state == null) return;
            if (state.isOf(Blocks.RESPAWN_ANCHOR)) {
                if (!mc.options.useKey.isPressed()) return;
                if (!HotbarUtils.isHolding(Items.GLOWSTONE)) return;
                new ScheduledTask(() -> {
                    HotbarUtils.search(Items.RESPAWN_ANCHOR);
                }).runDelayedTask(Randomizer.rand(50));
            }
        }
    }
}
