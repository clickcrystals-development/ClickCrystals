package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBreakEvent extends Event {

    private final BlockPos pos;
    private final BlockState state;
    private final LevelAccessor world;

    public BlockBreakEvent(BlockPos pos, BlockState state, LevelAccessor world) {
        this.pos = pos;
        this.state = state;
        this.world = world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public LevelAccessor getWorld() {
        return world;
    }
}
