package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

@FunctionalInterface
public interface BlockPunchListener {

    void pass(BlockPos pos, Direction dir);
}
