package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@FunctionalInterface
public interface BlockPunchListener {

    void pass(BlockPos pos, Direction dir);
}
