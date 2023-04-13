package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;

public class DiscordPresence extends Module implements Listener {

    private final DiscordRichPresence rpc;
    private long start;
    private boolean running;

    public DiscordPresence() {
        super("DiscordPresence", Categories.CRYSTALLING,"Show off to your friends that you have this wonderful mod!");
        this.rpc = new DiscordRichPresence();
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        this.running = true;
        DiscordRPC.discordInitialize("1096155629791494215", new DiscordEventHandlers.Builder()
                .setReadyEventHandler((user) -> {
                    System.out.println("Welcome to ClickCrystals, " + user.username + "#" + user.discriminator + "!");
                })
                .setDisconnectedEventHandler((errorCode, message) -> {
                    System.out.println("Discord Presence disconnected: " + message);
                    System.out.println("Error code: " + errorCode);
                })
                .build(),true);
        this.update();
        this.restart();
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        this.running = false;
        DiscordRPC.discordShutdown();
    }

    private void update() {
        rpc.largeImageKey = "icon";
        rpc.largeImageText = "ClickCrystals | Crystal Utility Client";
        rpc.details = "Best Crystal PvP 'Utility' Client!";

        ServerInfo info = mc.player.networkHandler.getServerInfo();
        if (info == null) {
            rpc.state = "Looking at title screen";
            return;
        }
        rpc.state = "Clicking Crystals on " + info.address + "!";

        DiscordRPC.discordUpdatePresence(rpc);
        DiscordRPC.discordRunCallbacks();
    }

    private void restart() {
        rpc.startTimestamp = start;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        if (!running) this.onDisable();
    }

    @EventHandler
    public void onJoin(PacketReceiveEvent e) {
        if (e.getPacket() instanceof LoginSuccessS2CPacket || e.getPacket() instanceof DisconnectS2CPacket) {
            this.update();
        }
    }
}
