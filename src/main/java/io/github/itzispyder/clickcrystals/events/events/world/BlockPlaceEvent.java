package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class BlockPlaceEvent extends Event implements Cancellable {

    private final BlockState state;
    private final BlockPos pos;
    private boolean cancelled;

    public BlockPlaceEvent(BlockState state, BlockPos pos) {
        this.state = state;
        this.pos = pos;
        this.cancelled = false;
    }

    public BlockState getState() {
        return state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Item getItem() {
        return state.getBlock().asItem();
    }

    public Block getBlock() {
        return state.getBlock();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
