package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.ItemUseEvent;

@FunctionalInterface
public interface ItemUseListener {

    void pass(ItemUseEvent e);
}
