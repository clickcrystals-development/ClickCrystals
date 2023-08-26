package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.client.networking.EntityStatusType;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
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
            .name("enemy-pop-message")
            .description("Message sent when enemy pops, %pops% for count, %player% for their name")
            .def("&7&n%player% &8&o popped &7&o(&e%pops%&7&o) &8totems!")
            .build()
    );
    public final ModuleSetting<String> enemyDeath = general.add(StringSetting.create()
            .name("enemy-death-message")
            .description("Message sent when enemy pops, %pops% for count, %player% for their name")
            .def("&7&n%player% &8&o died after popping &7&o(&e%pops%&7&o) &8totems!")
            .build()
    );
    public final ModuleSetting<Boolean> hightlightOwn = general.add(BooleanSetting.create()
            .name("show-own")
            .description("Toggle showing your own pops in chat")
            .def(true)
            .build()
    );
    public final ModuleSetting<String> ownText = general.add(StringSetting.create()
            .name("own-pop-name")
            .description("Want to replace your name with in pop messages?")
            .def("&6&nYou")
            .build()
    );

    private static final Map<String,Integer> totemPops = new HashMap<>();

    public TotemPops() {
        super("totem-pops", Categories.MISC, "Send messages when a player pops their totem.");
        system.addListener(this);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

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
                if (hightlightOwn.getVal()) {
                    name = StringUtils.color(ownText.getVal());
                } else {
                    return;
                }
            }

            if (status == EntityStatusType.TOTEM_POP) {
                setPops(ent,getPops(ent) + 1);

                if (isEnabled()) {
                    ChatUtils.sendPrefixMessage(compilePopMsg(ent, name, enemyPop.getVal()));
                }
            }
            else if (status == EntityStatusType.DEATH) {
                if (isEnabled()) {
                    ChatUtils.sendPrefixMessage(compilePopMsg(ent, name, enemyDeath.getVal()));
                }

                setPops(ent,0);
            }
        }
    }

    private String compilePopMsg(Entity ent, String entName, String msg) {
        return StringUtils.color(msg)
                .replaceAll("%pops%", "" + getPops(ent))
                .replaceAll("%player%", entName);
    }

    public int getPops(Entity ent) {
        return getOrDefault(totemPops.get(ent.getDisplayName().getString()),0);
    }

    private void setPops(Entity ent, int pops) {
        totemPops.put(ent.getDisplayName().getString(), pops);
    }

    private <T> T getOrDefault(T value, T def) {
        return value != null ? value : def;
    }
}
