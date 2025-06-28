package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.BlockBreakEvent;

@FunctionalInterface
public interface BlockBreakListener {

    void pass(BlockBreakEvent e);
}
