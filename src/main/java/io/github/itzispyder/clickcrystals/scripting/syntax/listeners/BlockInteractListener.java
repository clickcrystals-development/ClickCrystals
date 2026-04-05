package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;

@FunctionalInterface
public interface BlockInteractListener {

    void pass(BlockHitResult hit, InteractionHand hand);
}
