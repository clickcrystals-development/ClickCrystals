package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.util.ChatUtils;

/**
 * Listeners for chat events
 */
public class ChatEventListener implements Listener {

    @EventHandler
    public void onChatReceive(ChatReceiveEvent e) {
        try {
            this.handleChatCommands(e);
        }
        catch (Exception ignore) {}
    }

    private void handleChatCommands(ChatReceiveEvent e) {
        final String message = e.getMessage();
        final String s = message.toLowerCase();

        if (!s.contains("!cc ")) return;
        if (s.contains("-users")) ChatUtils.sendChatMessage("I am using ClickCrystals.");
    }
}
