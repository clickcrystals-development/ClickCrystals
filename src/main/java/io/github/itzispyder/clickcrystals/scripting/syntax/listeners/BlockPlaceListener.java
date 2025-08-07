package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;

@FunctionalInterface
public interface BlockPlaceListener {

    void pass(BlockPlaceEvent e);
}
