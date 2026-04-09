package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EntityStatuses extends ListenerModule {

    public EntityStatuses() {
        super("entity-statuses", Categories.CLIENT, "DEBUG: Sends received EntityStatusPackets in chat");
    }

    @EventHandler
    public void onReceiveStatus(PacketReceiveEvent e) {
        if (e.getPacket() instanceof ClientboundEntityEventPacket packet && PlayerUtils.valid()) {
            Entity ent = packet.getEntity(PlayerUtils.getWorld());
            int status = packet.getEventId();
            String name = ent instanceof Player p ? p.getName().getString() : ent.getType().getDescription().getString();
            ChatUtils.sendPrefixMessage("§7[§6Entity Status§7] §f%s §7sent status §f%s".formatted(name, status));
        }
    }
}
