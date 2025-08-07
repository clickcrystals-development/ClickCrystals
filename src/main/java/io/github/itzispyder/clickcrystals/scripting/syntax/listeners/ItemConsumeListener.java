package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.events.world.ItemConsumeEvent;

@FunctionalInterface
public interface ItemConsumeListener {

    void pass(ItemConsumeEvent e);
}
