package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.ItemUseEvent;

@FunctionalInterface
public interface ItemUseListener {

    void pass(ItemUseEvent e);
}
