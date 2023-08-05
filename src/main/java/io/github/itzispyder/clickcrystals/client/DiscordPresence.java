package io.github.itzispyder.clickcrystals.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import io.github.itzispyder.clickcrystals.ClickCrystals;

import static io.github.itzispyder.clickcrystals.ClickCrystals.prefix;
import static io.github.itzispyder.clickcrystals.ClickCrystals.version;

public class DiscordPresence {

    public static final String IDLE = "Idling...";
    public static final String DETAIL = "ClickCrystals on TOP!";
    public final DiscordRPC api = DiscordRPC.INSTANCE;
    public final String field_6969 = "1096155629791494215";
    private final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private final DiscordRichPresence rpc = new DiscordRichPresence();

    public DiscordPresence() {
        handlers.ready = user -> {
            String username = user.username + "#" + user.discriminator;
            System.out.println(prefix + "Welcome, " + username);
            ClickCrystals.info.put("discord", username);
        };
    }

    public void start() {
        api.Discord_Initialize(field_6969, handlers, true, null);
        rpc.startTimestamp = System.currentTimeMillis() / 1000L;
        rpc.largeImageKey = "icon";
        rpc.largeImageText = "ClickCrystals";
        rpc.smallImageKey = "small_icon";
        rpc.smallImageText = version;
        rpc.state = IDLE;
        rpc.details = DETAIL;
        update();
    }

    public void update() {
        api.Discord_UpdatePresence(rpc);
    }

    public void stop() {
        api.Discord_Shutdown();
    }

    public void setState(String state) {
        rpc.state = state;
        update();
    }

    public void setDetail(String detail) {
        rpc.details = detail;
        update();
    }

    public DiscordEventHandlers getHandlers() {
        return handlers;
    }

    public DiscordRichPresence getPresence() {
        return rpc;
    }
}
