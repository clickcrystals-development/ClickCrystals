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
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
        if (PlayerUtils.invalid() || mc.screen != null) return;
        if (event.getState().is(Blocks.RESPAWN_ANCHOR)) {
            clearPending();
            if (HotbarUtils.has(Items.GLOWSTONE))
                HotbarUtils.search(Items.GLOWSTONE);
        }
    }

    @EventHandler
    private void onSentPacket(PacketSentEvent event) {
        if (!(event.getPacket() instanceof ServerboundUseItemOnPacket packet)) return;
        if (PlayerUtils.invalid() || mc.screen != null) return;
        if (!HotbarUtils.isHolding(Items.GLOWSTONE) || !HotbarUtils.has(Items.RESPAWN_ANCHOR)) return;
        BlockPos pos = packet.getHitResult().getBlockPos();
        BlockState state = mc.level.getBlockState(pos);
        if (!state.is(Blocks.RESPAWN_ANCHOR)) return;
        if (state.getValue(RespawnAnchorBlock.CHARGE) != 0) return;
        pendingCharge = pos.immutable();
        timeoutTicks = 12;
    }

    @EventHandler
    private void onTick(ClientTickEndEvent event) {
        if (pendingCharge == null) return;
        if (PlayerUtils.invalid() || mc.screen != null || timeoutTicks-- <= 0) {
            clearPending();
            return;
        }
        BlockState state = mc.level.getBlockState(pendingCharge);
        if (!state.is(Blocks.RESPAWN_ANCHOR)) {
            clearPending();
            return;
        }
        if (state.getValue(RespawnAnchorBlock.CHARGE) <= 0) return;

        // Only switch if still looking at the same charged anchor with glowstone.
        if (mc.hitResult instanceof BlockHitResult hit
                && hit.getBlockPos().equals(pendingCharge)
                && HotbarUtils.isHolding(Items.GLOWSTONE)) {
            HotbarUtils.search(Items.RESPAWN_ANCHOR);
        }
        clearPending();
    }
}
