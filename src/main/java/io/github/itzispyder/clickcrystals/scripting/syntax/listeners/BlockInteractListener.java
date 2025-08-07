package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

@FunctionalInterface
public interface BlockInteractListener {

    void pass(BlockHitResult hit, Hand hand);
}
