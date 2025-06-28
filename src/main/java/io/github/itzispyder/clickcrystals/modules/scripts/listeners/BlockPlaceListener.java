package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;

@FunctionalInterface
public interface BlockPlaceListener {

    void pass(BlockPlaceEvent e);
}
