package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockPlaceEvent extends Event {

    private final BlockState state;
    private final BlockPos pos;

    public BlockPlaceEvent(BlockState state, BlockPos pos) {
        this.state = state;
        this.pos = pos;
    }

    public BlockState state() {
        return state;
    }

    public BlockPos pos() {
        return pos;
    }
}
