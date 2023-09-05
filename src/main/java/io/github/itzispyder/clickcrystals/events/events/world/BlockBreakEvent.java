package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class BlockBreakEvent extends Event {

    private final BlockPos pos;
    private final BlockState state;
    private final WorldAccess world;

    public BlockBreakEvent(BlockPos pos, BlockState state, WorldAccess world) {
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

    public WorldAccess getWorld() {
        return world;
    }
}
