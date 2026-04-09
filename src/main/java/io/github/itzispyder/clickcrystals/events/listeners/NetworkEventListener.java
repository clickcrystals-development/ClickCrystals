package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.commands.commands.pixelart.PixelArtGenerator;
import io.github.itzispyder.clickcrystals.client.system.BulletinBoard;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsGate;
import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.GameJoinEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameLeaveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scripting.syntax.logic.AsCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;

import java.net.URI;
import java.util.UUID;

import static io.github.itzispyder.clickcrystals.ClickCrystals.commandPrefix;
import static io.github.itzispyder.clickcrystals.ClickCrystals.config;

public class NetworkEventListener implements Listener {

    @EventHandler
    public void onPacketSend(PacketSendEvent e) {
        try {
            this.handlePixelArtStop(e);
        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        try {
            this.handPlayerJoin(e);
            this.handlePixelArtStop(e);
            this.handleHudConfigLoading(e);
        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onGameJoin(GameJoinEvent e) {
        try {
            TickEventListener.cancelTickInputs(); // stops tick inputs
            AsCmd.resetReferenceEntity();

            this.handleCheckUpdates();
            BulletinBoard.request();
            Module.disableAllGameJoinDisabled();
            system.cameraRotator.closeAllTickets();
        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onGameLeave(GameLeaveEvent e) {
        try {
            Notification.clearNotifications();
            BulletinBoard.request();
            system.cameraRotator.closeAllTickets();
        }
        catch (Exception ignore) {}
    }

    private void handPlayerJoin(PacketReceiveEvent e) {
        if (e.getPacket() instanceof ClientboundPlayerInfoUpdatePacket packet) {
            if (packet.actions().stream().anyMatch(a -> a == ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER)) {
                system.cameraRotator.closeAllTickets(); // stops current rotator

                // broadcasting joins
                for (ClientboundPlayerInfoUpdatePacket.Entry entry : packet.newEntries()) {
                    UUID id = entry.profile().id();
                    String name = entry.profile().name();

                    if (ClickCrystals.info.getOwner(id) != null) {
                        Notification.create()
                                .ccIcon()
                                .id("cc-staff-join")
                                .title("&eCC Staff Joined!")
                                .text("&7%s, &bA ClickCrystals &7%s&b, has joined the game!".formatted(name, "DEV"))
                                .stayTime(6000)
                                .build()
                                .sendToClient();
                    }
                    else if (ClickCrystals.info.getStaff(id) != null) {
                        Notification.create()
                                .ccIcon()
                                .id("cc-staff-join")
                                .title("&eCC Staff Joined!")
                                .text("&7%s, &bA ClickCrystals &7%s&b, has joined the game!".formatted(name, "staff"))
                                .stayTime(6000)
                                .build()
                                .sendToClient();
                    }
                }
            }
        }
    }

    private void handleHudConfigLoading(PacketReceiveEvent e) {
        if (e.getPacket() instanceof ClientboundLoginFinishedPacket) {
            config.loadHuds();
            config.save(); // saving just to be safe
        }
    }

    private void handlePixelArtStop(PacketReceiveEvent e) {
        Packet<?> p = e.getPacket();
        if (p instanceof ClientboundDisconnectPacket || p instanceof ClientboundHelloPacket) {
            PixelArtGenerator.cancel();
        }
    }

    private void handlePixelArtStop(PacketSendEvent e) {
        Packet<?> p = e.getPacket();
        if (p instanceof ServerboundHelloPacket || p instanceof ServerboundKeyPacket) {
            PixelArtGenerator.cancel();
        }
    }

    private void handleCheckUpdates() {
        ClickCrystalsGate gate = new ClickCrystalsGate();
        if (gate.isBanned()) {
            system.scheduler.runDelayedTask(gate::banishCurrentSession, 1000);
            return;
        }

        if (!ClickCrystals.matchLatestVersion()) {
            system.scheduler.runChainTask()
                    .thenWait(5 * 1000)
                    .thenRun(() -> mc.execute(() -> {
                        ChatUtils.sendBlank();
                        ChatUtils.sendWarningMessage("§bClickCrystals is §e§nNOT UP TO DATE§b! Get the newest version now!");
                        ChatUtils.sendPrefixMessage("§bYour Version=§7" + version + "§b, §oNewest Version=§7" + ClickCrystals.getLatestVersion());

                        try {
                            Component literal = Component.literal(starter + "§a§o§n https://www.curseforge.com/minecraft/mc-mods/clickcrystals");
                            ClickEvent event = new ClickEvent.OpenUrl(new URI("https://www.curseforge.com/minecraft/mc-mods/clickcrystals"));
                            MutableComponent text = literal.copy();
                            ChatUtils.sendRawText(text.withStyle(text.getStyle().withClickEvent(event)));
                            ChatUtils.sendBlank();
                        }
                        catch (Exception ignore) {}
                    }))
                    .thenRepeat(ChatUtils::pingPlayer, 10 * 50, 3)
                    .thenWait(2 * 1000)
                    .thenRun(this::notifyRemindUpdate)
                    .startChain();
        }
        else if (!config.hasPlayedBefore()) {
            system.scheduler.runDelayedTask(this::notifyOpenMenu, 2 * 1000);
        }
    }

    public void notifyRemindUpdate() {
        Notification.create()
                .id("update-reminder")
                .title("&bUpdate ClickCrystals!")
                .text("§bClickCrystals is §e§nNOT UP TO DATE§b! Get the newest version now!")
                .ccIcon()
                .stayTime(6000)
                .build()
                .sendToClient();
    }

    public void notifyOpenMenu() {
        Notification.create()
                .id("new-user-tips")
                .title("Open ClickCrystals' Menu")
                .text("&7Press apostrophe &f[&b'&f]&7 key to open client menu!")
                .ccIcon()
                .stayTime(5000)
                .build()
                .sendToClient();

        Notification.create()
                .id("new-user-tips")
                .title("Stuck?")
                .text("&7Type command &f%1$skeybinds&7 or &f%1$stoggle&7 as an alternative.".formatted(commandPrefix.getKeyName()))
                .ccIcon()
                .stayTime(3000)
                .build()
                .sendToClient();

        Notification.create()
                .id("new-user-tips")
                .title("Still Stuck?")
                .text("&dJoin The Discord &7for help from our staff!")
                .ccIcon()
                .stayTime(2000)
                .build()
                .sendToClient();
    }
}
