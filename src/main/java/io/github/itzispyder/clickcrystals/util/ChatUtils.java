package io.github.itzispyder.clickcrystals.util;

import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

/**
 * Client chat utils
 */
public abstract class ChatUtils {

    /**
     * Sends a message to the player client-side
     * @param message message
     */
    public static void sendMessage(String message) {
         if (message == null) return;
         mc.player.sendMessage(Text.literal(message));
    }
    public static void sendChat(String message) {
        if (message == null) return;
        mc.player.networkHandler.sendChatMessage(message);
    }

    /**
     * Sends a message to the player client-sided, but with the mod prefix
     * @param message message
     */
    public static void sendPrefixMessage(String message) {
        sendMessage(starter + message);
    }

    public static void sendChatCommand(String cmd) {
        mc.player.networkHandler.sendCommand(cmd);
    }

    public static void sendChatMessage(String msg) {
        mc.player.networkHandler.sendChatMessage(msg);
    }
}
