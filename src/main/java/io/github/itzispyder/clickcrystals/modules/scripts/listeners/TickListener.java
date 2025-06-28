package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.Event;

@FunctionalInterface
public interface TickListener {

    void pass(Event e);
}
