package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.data.pixelart.PixelArtGenerator;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.scheduler.Scheduler;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.version;

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
            this.handleCheckUpdates(e);
            this.handlePixelArtStop(e);
            this.handleHudConfigLoading(e);
        }
        catch (Exception ignore) {}
    }

    private void handleHudConfigLoading(PacketReceiveEvent e) {
        if (e.getPacket() instanceof LoginSuccessS2CPacket) {
            Hud.loadConfigHuds();
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

    private void handleCheckUpdates(PacketReceiveEvent e) {
        if (e.getPacket() instanceof LoginSuccessS2CPacket && !ClickCrystals.matchLatestVersion()) {
            Scheduler.runTaskLater(() -> mc.execute(() -> {
                ChatUtils.sendBlank();
                ChatUtils.sendWarningMessage("§bClickCrystals is §e§nNOT UP TO DATE§b! Get the newest version now!");
                ChatUtils.sendPrefixMessage("§bYour Version=§7" + version + "§b, §oNewest Version=§7" + ClickCrystals.getLatestVersion());

                Text literal = Text.literal(ClickCrystals.starter + "§a§o§n https://modrinth.com/mod/clickcrystals");
                ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/clickcrystals");
                MutableText text = literal.copy();

                ChatUtils.sendRawText(text.fillStyle(text.getStyle().withClickEvent(event)));
                ChatUtils.sendBlank();
            }), 60);

            Runnable task = ChatUtils::pingPlayer;
            Scheduler.runTaskLater(task, 60);
            Scheduler.runTaskLater(task, 70);
            Scheduler.runTaskLater(task, 80);
        }
    }
}
