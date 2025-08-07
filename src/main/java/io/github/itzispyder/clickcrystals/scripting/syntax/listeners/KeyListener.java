package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;

@FunctionalInterface
public interface KeyListener {

    void pass(KeyPressEvent e);
}
