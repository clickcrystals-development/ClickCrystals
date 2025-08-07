package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;


import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;

@FunctionalInterface
public interface ClickListener {

    void pass(MouseClickEvent e);
}
