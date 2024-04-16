package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;

public class NoResPack extends Module implements Listener {

    public NoResPack() {
        super("no-server-packs", Categories.LAG,"Prevents servers from forcing you to download their bad resource pack");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onResourceReceive(PacketReceiveEvent e) {
        if (e.getPacket() instanceof ResourcePackSendS2CPacket packet) {
            e.setCancelled(true);
            String status = packet.required() ? "forced" : "suggested";
            ChatUtils.sendPrefixMessage("Blocked 1 " + status + " resource pack");
        }
    }

    @EventHandler
    private void onResourceSend(PacketSendEvent e) {
        if (e.getPacket() instanceof ResourcePackStatusC2SPacket packet) {
            e.setCancelled(true);
        }
    }
}
