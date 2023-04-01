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

    /**
     * Sends a command as if the player typed it
     * @param command command message
     */
    public static void sendCommand(String command) {
        if (command == null) return;
        mc.player.networkHandler.sendCommand(command);
    }

    /**
     * Sends a message to the chat as if a player has typed it
     * @param message message
     */
    public static void sendChatMessage(String message) {
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
}
