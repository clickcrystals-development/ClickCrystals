package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;

public class ChatEventListener implements Listener {

    @EventHandler
    public void onChatSend(ChatSendEvent e) {
        String message = e.getMessage();
        
        // intercept comma commands for mobile-friendly usage
        if (message.startsWith(",")) {
            String command = message.substring(1);
            try {
                Command.dispatch(command);
            }
            catch (Exception ignore) {}
            e.cancel();
        }
    }

    @EventHandler
    public void onChatReceive(ChatReceiveEvent e) {

    }
}
