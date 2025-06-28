package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;

@FunctionalInterface
public interface ChatSendListener {

    void pass(ChatSendEvent e);
}
