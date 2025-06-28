package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;

@FunctionalInterface
public interface KeyListener {

    void pass(KeyPressEvent e);
}
