package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;

@FunctionalInterface
public interface ChatReceiveListener {

    void pass(ChatReceiveEvent e);
}
