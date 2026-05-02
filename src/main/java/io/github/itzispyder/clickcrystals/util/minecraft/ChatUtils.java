package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.misc.ClientTheme;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public final class ChatUtils implements Global {

    /**
     * Builds the {@code [ClickCrystals]} chat prefix as a Component using the
     * current theme color.
     */
    private static MutableComponent buildPrefix() {
        int primary = ClientTheme.primary() & 0x00FFFFFF;
        int dim = ClientTheme.primaryDim() & 0x00FFFFFF;
        return Component.literal("[")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x777777)))
                .append(Component.literal("Click")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(primary))))
                .append(Component.literal("Crystals")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(dim))))
                .append(Component.literal("] ")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x777777))));
    }

    public static void sendMessage(String message) {
        if (message != null && PlayerUtils.valid()) {
            PlayerUtils.player().sendSystemMessage(Component.literal(message));
        }
    }

    public static void sendPrefixMessage(String message) {
        if (message != null && PlayerUtils.valid()) {
            PlayerUtils.player().sendSystemMessage(buildPrefix().append(Component.literal(message)));
        }
    }

    public static void sendWarningMessage(String message) {
        if (message != null && PlayerUtils.valid()) {
            PlayerUtils.player()
                    .sendSystemMessage(buildPrefix().append(Component.literal("§8(§eWarning§8)§r " + message)));
        }
    }

    public static void sendRawText(Component text) {
        if (PlayerUtils.valid() && text != null) {
            PlayerUtils.player().sendSystemMessage(text);
        }
    }

    public static void sendSevereMessage(String message) {
        if (message != null && PlayerUtils.valid()) {
            PlayerUtils.player()
                    .sendSystemMessage(buildPrefix().append(Component.literal("§8(§c§lError§8)§r " + message)));
        }
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
        SoundInstance sound = SimpleSoundInstance.forUI(event, 10.0F, 0.1F);
        mc.execute(() -> sm.play(sound));
    }
}