package io.github.itzispyder.clickcrystals.client.system;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;
import static io.github.itzispyder.clickcrystals.ClickCrystals.version;

public class DiscordPresence {

    public static final String IDLE = "Idling...";
    public static final String DETAIL = "ClickCrystals on TOP!";
    public final String field_6969 = "1096155629791494215";
    public DiscordRPC api;
    private DiscordEventHandlers handlers;
    private DiscordRichPresence rpc;
    public boolean loadedSuccessfully;

    public DiscordPresence() {
        try {
            api = DiscordRPC.INSTANCE;
            rpc = new DiscordRichPresence();
            handlers = new DiscordEventHandlers();
            handlers.ready = user -> system.printf("Welcome, %s#%s", user.username, user.discriminator);
            loadedSuccessfully = true;
        }
        catch (Exception ex) {
            loadedSuccessfully = false;
            ex.printStackTrace();
        }
    }

    public void start() {
        if (loadedSuccessfully) {
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
    }

    public void update() {
        if (loadedSuccessfully) {
            api.Discord_UpdatePresence(rpc);
        }
    }

    public void stop() {
        if (loadedSuccessfully) {
            api.Discord_Shutdown();
        }
    }

    public void setState(String state) {
        if (loadedSuccessfully) {
            rpc.state = state;
            update();
        }
    }

    public void setDetail(String detail) {
        if (loadedSuccessfully) {
            rpc.details = detail;
            update();
        }
    }
}
