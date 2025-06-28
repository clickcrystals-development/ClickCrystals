package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.ItemConsumeEvent;

@FunctionalInterface
public interface ItemConsumeListener {

    void pass(ItemConsumeEvent e);
}
