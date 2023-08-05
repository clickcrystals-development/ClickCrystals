package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSentEvent;
import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

public class AnchorSwitch extends Module implements Listener {

    public AnchorSwitch() {
        super("anchor-switch", Categories.ANCHORING,"Whenever you place an anchor, switch to glowstone then back after it has been charged.");
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
    private void onPlaceBlock(BlockPlaceEvent event) {
        if (event.state().getBlock() != Blocks.RESPAWN_ANCHOR) return;
        HotbarUtils.search(Items.GLOWSTONE);
    }

    @EventHandler
    private void onSendPacket(PacketSendEvent event) {
        if (!(event.getPacket() instanceof PlayerInteractBlockC2SPacket p)) return;
        BlockState state = mc.world.getBlockState(p.getBlockHitResult().getBlockPos());

        if (state.getBlock() != Blocks.RESPAWN_ANCHOR) return;
        if (!HotbarUtils.has(Items.GLOWSTONE) || !HotbarUtils.isHolding(Items.GLOWSTONE)) return;

        if (state.get(RespawnAnchorBlock.CHARGES) > 0) {
            HotbarUtils.search(Items.RESPAWN_ANCHOR);
        }
    }

    @EventHandler
    private void onSentPacket(PacketSentEvent event) {
        if (!(event.getPacket() instanceof PlayerInteractBlockC2SPacket p)) return;

        BlockState state = mc.world.getBlockState(p.getBlockHitResult().getBlockPos());

        if (state.getBlock() != Blocks.RESPAWN_ANCHOR) return;
        if (!HotbarUtils.has(Items.GLOWSTONE)) return;

        if (state.get(RespawnAnchorBlock.CHARGES) == 0) {
            HotbarUtils.search(Items.GLOWSTONE);
        }
    }
}