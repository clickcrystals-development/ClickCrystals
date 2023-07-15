package io.github.itzispyder.clickcrystals.util;

import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

public final class ChatUtils {

    public static void sendMessage(String message) {
        if (message == null) return;
        if (mc.player == null) return;
        mc.player.sendMessage(Text.literal(message));
    }

    public static void sendPrefixMessage(String message) {
        sendMessage(starter + message);
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
}
