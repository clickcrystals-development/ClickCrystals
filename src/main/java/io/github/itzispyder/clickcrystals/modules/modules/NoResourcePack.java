package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;

/**
 * NoResourcePack module
 */
public class NoResourcePack extends Module implements Listener {

    public NoResourcePack() {
        super("NoResourcePack", Categories.MISC,"Blocks server required and suggested resource packs!");
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
            String status = packet.isRequired() ? "forced" : "suggested";
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
