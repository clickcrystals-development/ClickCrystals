package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.client.client.CCSoundEvents;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.CCExtras;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Listeners for chat events
 */
public class ChatEventListener implements Listener {

    @EventHandler
    public void onChatSend(ChatSendEvent e) {

    }

    @EventHandler
    public void onChatReceive(ChatReceiveEvent e) {
        try {
            this.handleChatCommands(e);
        }
        catch (Exception ignore) {}
    }

    private void handleChatCommands(ChatReceiveEvent e) {
        final ClientPlayerEntity p = mc.player;
        final String message = e.getMessage();
        final String s = message.toLowerCase();
        final Module ccExtras = Module.get(CCExtras.class);

        if (!ccExtras.isEnabled()) return;
        if (p == null) return;

        if (!s.contains("!cc ")) return;
        if (s.contains("-users")) ChatUtils.sendChatMessage("I am using ClickCrystals.");
        if (s.contains("-vineboom")) p.playSound(CCSoundEvents.VINEBOOM, SoundCategory.MASTER, 10.0F, 1.0F);
    }
}
