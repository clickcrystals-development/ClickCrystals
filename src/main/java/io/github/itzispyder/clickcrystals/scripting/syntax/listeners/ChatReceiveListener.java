package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;

@FunctionalInterface
public interface ChatReceiveListener {

    void pass(ChatReceiveEvent e);
}
