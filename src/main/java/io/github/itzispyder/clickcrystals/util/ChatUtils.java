package io.github.itzispyder.clickcrystals.util;

import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

/**
 * Chat utils for the client player entity
 */
public abstract class ChatUtils {

    public static void sendMessage(String message) {
         if (message == null) return;
         mc.player.sendMessage(Text.literal(message));
    }

    public static void sendCommand(String command) {
        if (command == null) return;
        mc.player.networkHandler.sendCommand(command);
    }

    public static void sendChatMessage(String message) {
        if (message == null) return;
        mc.player.networkHandler.sendChatMessage(message);
    }

    public static void sendPrefixMessage(String message) {
        sendMessage(starter + message);
    }
}
