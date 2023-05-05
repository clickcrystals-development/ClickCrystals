package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.client.CCKeybindings;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ChatCommandEvent;
import io.github.itzispyder.clickcrystals.events.events.ChatSendEvent;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;

public class MsgResend extends Module implements Listener {

    private String lastMessage;
    private boolean wasCommand;

    public MsgResend() {
        super("MsgResend", Categories.OTHER, "Press up arrow key to resend your last message.");
        this.lastMessage = null;
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onChatSend(ChatSendEvent e) {
        final String msg = e.getMessage();

        this.lastMessage = msg;
        this.wasCommand = false;
    }

    @EventHandler
    private void onChatSend(ChatCommandEvent e) {
        final String msg = e.getCommandLine();

        this.lastMessage = msg;
        this.wasCommand = true;
    }

    @EventHandler
    private void onTickEnd(ClientTickEndEvent e) {
        if (this.lastMessage == null) return;
        if (this.lastMessage.isBlank()) return;
        if (this.lastMessage.isEmpty()) return;

        if (CCKeybindings.SEND_LAST_MESSAGE.wasPressed()) {
            if (wasCommand) {
                ChatUtils.sendChatCommand(lastMessage);
            }
            else {
                ChatUtils.sendChatMessage(lastMessage);
            }

            CCKeybindings.SEND_LAST_MESSAGE.setPressed(false);
        }
    }
}
