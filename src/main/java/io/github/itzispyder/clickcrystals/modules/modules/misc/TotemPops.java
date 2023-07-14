package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.client.EntityStatusType;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.*;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;
import java.util.Map;

public class TotemPops extends Module implements Listener {

    public final SettingSection general = getGeneralSection();
    public final ModuleSetting<String> enemyPop = general.add(StringSetting.create()
            .name("Enemy Pop Message")
            .description("Message sent when enemy pops, %pops% for count, %player% for their name")
            .def("&7&n%player% &8&o popped &7&o(&e%pops%&7&o) &8totems!")
            .build()
    );
    public final ModuleSetting<String> enemyDeath = general.add(StringSetting.create()
            .name("Enemy Death Message")
            .description("Message sent when enemy pops, %pops% for count, %player% for their name")
            .def("&7&n%player% &8&o died after popping &7&o(&e%pops%&7&o) &8totems!")
            .build()
    );
    public final ModuleSetting<Boolean> showOwn = general.add(BooleanSetting.create()
            .name("Show Own")
            .description("Toggle showing your own pops in chat")
            .def(true)
            .build()
    );
    public final ModuleSetting<String> ownText = general.add(StringSetting.create()
            .name("Own pop name")
            .description("Your name in own pop messages %name% for your name")
            .def("&6&oYou")
            .build()
    );

    private static final Map<String,Integer> totemPops = new HashMap<>();

    public TotemPops() {
        super("totem-pops", Categories.MISC, "Send messages when a player pops their totem.");
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
    private void onReceiveStatus(PacketReceiveEvent e) {
        if (e.getPacket() instanceof EntityStatusS2CPacket packet) {
            if (mc.player == null) return;
            if (mc.player.getWorld() == null) return;

            final Entity ent = packet.getEntity(mc.player.getWorld());
            final int status = packet.getStatus();

            if (ent == null) return;
            if (ent.getType() != EntityType.PLAYER) return;

            String name = ent.getDisplayName().getString();
            if (name.equals(mc.player.getDisplayName().getString())) {
                if (showOwn.getVal()) {
                    name = StringUtils.color(ownText.getVal().replace("%name%",name));
                } else {
                    return;
                }
            }

            if (status == EntityStatusType.TOTEM_POP) {
                setPops(ent,getPops(ent) + 1);
                ChatUtils.sendPrefixMessage(StringUtils.color(enemyPop.getVal().replace("%player%",name)));
            }
            else if (status == EntityStatusType.DEATH) {
                ChatUtils.sendPrefixMessage(StringUtils.color(enemyDeath.getVal().replace("%player%",name)));
                setPops(ent,0);
            }
        }
    }

    private int getPops(Entity ent) {
        return getOrDefault(totemPops.get(ent.getDisplayName().getString()),0);
    }

    private void setPops(Entity ent, int pops) {
        totemPops.put(ent.getDisplayName().getString(), pops);
    }

    private <T> T getOrDefault(T value, T def) {
        return value != null ? value : def;
    }
}
