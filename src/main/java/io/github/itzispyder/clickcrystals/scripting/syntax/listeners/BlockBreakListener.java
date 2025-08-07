package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.BlockBreakEvent;

@FunctionalInterface
public interface BlockBreakListener {

    void pass(BlockBreakEvent e);
}
