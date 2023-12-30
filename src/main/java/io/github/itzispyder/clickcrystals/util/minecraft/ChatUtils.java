package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public final class ChatUtils implements Global {

    public static void sendMessage(String message) {
        if (message == null) return;
        if (mc.player == null) return;
        mc.player.sendMessage(Text.literal(message));
    }

    public static void sendPrefixMessage(String message) {
        sendMessage(starter + message);
    }

    public static void sendWarningMessage(String message) {
        sendMessage(starter + "§8(§eWarning§8)§r " + message);
    }

    public static void sendRawText(Text text) {
        if (mc.player != null && text != null) {
            mc.player.sendMessage(text);
        }
    }

    public static void sendSevereMessage(String message) {
        sendMessage(starter + "§8(§c§lError§8)§r " + message);
    }

    public static void sendChatCommand(String cmd) {
        if (mc.player == null) return;
        mc.player.networkHandler.sendCommand(cmd);
    }

    public static void sendChatMessage(String msg) {
        if (mc.player == null) return;
        mc.player.networkHandler.sendChatMessage(msg);
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
        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F, 10.0F));
    }
}
