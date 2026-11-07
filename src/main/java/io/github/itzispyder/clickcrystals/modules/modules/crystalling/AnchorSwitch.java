package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSentEvent;
import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;

/**
 * Selects glowstone after anchor placement and returns to the anchor only
 * when a charge is observable in the local world. Never detonates for the user.
 */
@ModrinthNoNo
public class AnchorSwitch extends Module implements Listener {
    private BlockPos pendingCharge;
    private int timeoutTicks;

    public AnchorSwitch() {
        super("anchor-switch", Categories.CRYSTAL,
                "Switch to glowstone after placing an anchor; return to anchor once charged");
    }

    @Override protected void onEnable() {
        clearPending();
        system.addListener(this);
    }

    @Override protected void onDisable() {
        system.removeListener(this);
        clearPending();
    }

    private void clearPending() {
        pendingCharge = null;
        timeoutTicks = 0;
    }

    @EventHandler
    private void onBlockPlaced(BlockPlaceEvent event) {
        if (PlayerUtils.invalid() || mc.currentScreen != null) return;
        if (event.getState().isOf(Blocks.RESPAWN_ANCHOR)) {
            clearPending();
            if (HotbarUtils.has(Items.GLOWSTONE))
                HotbarUtils.search(Items.GLOWSTONE);
        }
    }

    @EventHandler
    private void onSentPacket(PacketSentEvent event) {
        if (!(event.getPacket() instanceof PlayerInteractBlockC2SPacket packet)) return;
        if (PlayerUtils.invalid() || mc.currentScreen != null) return;
        if (!HotbarUtils.isHolding(Items.GLOWSTONE) || !HotbarUtils.has(Items.RESPAWN_ANCHOR)) return;
        BlockPos pos = packet.getBlockHitResult().getBlockPos();
        BlockState state = mc.world.getBlockState(pos);
        if (!state.isOf(Blocks.RESPAWN_ANCHOR)) return;
        if (state.get(RespawnAnchorBlock.CHARGES) != 0) return;
        pendingCharge = pos.toImmutable();
        timeoutTicks = 12;
    }

    @EventHandler
    private void onTick(ClientTickEndEvent event) {
        if (pendingCharge == null) return;
        if (PlayerUtils.invalid() || mc.currentScreen != null || timeoutTicks-- <= 0) {
            clearPending();
            return;
        }
        BlockState state = mc.world.getBlockState(pendingCharge);
        if (!state.isOf(Blocks.RESPAWN_ANCHOR)) {
            clearPending();
            return;
        }
        if (state.get(RespawnAnchorBlock.CHARGES) <= 0) return;

        // Only switch if still looking at the same charged anchor with glowstone.
        if (mc.crosshairTarget instanceof BlockHitResult hit
                && hit.getBlockPos().equals(pendingCharge)
                && HotbarUtils.isHolding(Items.GLOWSTONE)) {
            HotbarUtils.search(Items.RESPAWN_ANCHOR);
        }
        clearPending();
    }
}
