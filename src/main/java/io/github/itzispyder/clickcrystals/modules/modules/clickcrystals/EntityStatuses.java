package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class EntityStatuses extends ListenerModule {

    public EntityStatuses() {
        super("entity-statuses", Categories.CLIENT, "DEBUG: Sends received EntityStatusPackets in chat");
    }

    @EventHandler
    public void onReceiveStatus(PacketReceiveEvent e) {
        if (e.getPacket() instanceof EntityStatusS2CPacket packet && PlayerUtils.valid()) {
            Entity ent = packet.getEntity(PlayerUtils.getWorld());
            int status = packet.getStatus();
            String name = ent instanceof PlayerEntity p ? p.getName().getString() : ent.getType().getName().getString();
            ChatUtils.sendPrefixMessage("§7[§6Entity Status§7] §f%s §7sent status §f%s".formatted(name, status));
        }
    }
}
