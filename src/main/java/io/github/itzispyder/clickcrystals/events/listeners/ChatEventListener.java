package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.ChatSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.AntiCCOptout;
import io.github.itzispyder.clickcrystals.util.ChatUtils;

/**
 * Listeners for chat events
 */
public class ChatEventListener implements Listener {

    @EventHandler
    public void onChatSend(ChatSendEvent e) {
        try {

        }
        catch(Exception ignore) {}
    }

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
        final Module antiOptout = Module.get(AntiCCOptout.class);

        if (antiOptout.isEnabled()) return;

        if (!s.contains("!cc ")) return;
        if (s.contains("-users")) ChatUtils.sendChatMessage("I am using ClickCrystals.");
    }
}
