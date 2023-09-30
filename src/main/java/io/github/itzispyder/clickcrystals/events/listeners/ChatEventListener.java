package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;

/**
 * Listeners for chat events
 */
public class ChatEventListener implements Listener {

    @EventHandler
    public void onChatSend(ChatSendEvent e) {

    }

    @EventHandler
    public void onChatReceive(ChatReceiveEvent e) {

    }
}
