package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public final class ChatUtils implements Global {

    public static void sendMessage(String message) {
        if (message != null && PlayerUtils.valid()) {
            PlayerUtils.player().displayClientMessage(Component.literal(message),false);
        }
    }

    public static void sendPrefixMessage(String message) {
        sendMessage(starter + message);
    }

    public static void sendWarningMessage(String message) {
        sendMessage(starter + "§8(§eWarning§8)§r " + message);
    }

    public static void sendRawText(Component text) {
        if (PlayerUtils.valid() && text != null) {
            PlayerUtils.player().displayClientMessage(text,false);
        }
    }

    public static void sendSevereMessage(String message) {
        sendMessage(starter + "§8(§c§lError§8)§r " + message);
    }

    public static void sendChatCommand(String cmd) {
        if (PlayerUtils.valid()) {
            PlayerUtils.player().connection.sendCommand(cmd);
        }
    }

    public static void sendChatMessage(String msg) {
        if (PlayerUtils.valid()) {
            PlayerUtils.player().connection.sendChat(msg);
        }
    }

    public static void sendBlank(int lines) {
        for (int i = 0; i < lines; i++) {
            sendMessage("");
        }
    }

    public static void sendBlank() {
        sendBlank(1);
    }

    public static void pingPlayer() {
        SoundManager sm = mc.getSoundManager();
        SoundEvent event = SoundEvents.EXPERIENCE_ORB_PICKUP;
        SoundInstance sound = SimpleSoundInstance.forUI(event, 0.1F, 10.0F);
        mc.execute(() -> sm.play(sound));
    }
}
