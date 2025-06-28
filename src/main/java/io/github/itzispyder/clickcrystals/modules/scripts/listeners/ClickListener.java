package io.github.itzispyder.clickcrystals.modules.scripts.listeners;


import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;

@FunctionalInterface
public interface ClickListener {

    void pass(MouseClickEvent e);
}
