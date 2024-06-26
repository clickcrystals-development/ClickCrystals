package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.client.ClickCrystalsGate;
import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.data.announce.BulletinBoard;
import io.github.itzispyder.clickcrystals.data.pixelart.PixelArtGenerator;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.GameJoinEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameLeaveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.AsCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

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
        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onGameLeave(GameLeaveEvent e) {
        try {
            Notification.clearNotifications();
            BulletinBoard.request();
        }
        catch (Exception ignore) {}
    }

    private void handPlayerJoin(PacketReceiveEvent e) {
        if (e.getPacket() instanceof PlayerListS2CPacket packet) {
            if (packet.getActions().stream().anyMatch(a -> a == PlayerListS2CPacket.Action.ADD_PLAYER)) {
                CameraRotator.cancelCurrentRotator(); // stops current rotator

                // broadcasting joins
                for (PlayerListS2CPacket.Entry entry : packet.getPlayerAdditionEntries()) {
                    UUID id = entry.profile().getId();
                    String name = entry.profile().getName();

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
        if (e.getPacket() instanceof LoginSuccessS2CPacket) {
            config.loadHuds();
            config.save(); // saving just to be safe
        }
    }

    private void handlePixelArtStop(PacketReceiveEvent e) {
        Packet<?> p = e.getPacket();
        if (p instanceof DisconnectS2CPacket || p instanceof LoginHelloS2CPacket) {
            PixelArtGenerator.cancel();
        }
    }

    private void handlePixelArtStop(PacketSendEvent e) {
        Packet<?> p = e.getPacket();
        if (p instanceof LoginHelloC2SPacket || p instanceof LoginKeyC2SPacket) {
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

                        Text literal = Text.literal(starter + "§a§o§n https://www.curseforge.com/minecraft/mc-mods/clickcrystals");
                        ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/clickcrystals");
                        MutableText text = literal.copy();

                        ChatUtils.sendRawText(text.fillStyle(text.getStyle().withClickEvent(event)));
                        ChatUtils.sendBlank();
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
