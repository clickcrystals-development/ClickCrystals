package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;
import java.util.Map;

public class TotemPops extends Module implements Listener {

    public final SettingSection general = getGeneralSection();
    public final ModuleSetting<String> enemyPops = general.add(StringSetting.create()
            .name("enemy-pop-message")
            .description("Message sent when enemy pops, %pops% for count, %enemy% for their name")
            .def("&8&oThe player &4%enemy%&8&o popped &7&o(&e%pops%&7&o) &8totems!")
            .build()
    );
    public final ModuleSetting<String> enemyDeath = general.add(StringSetting.create()
            .name("enemy-death-message")
            .description("Message sent when enemy pops, %pops% for count, %enemy% for their name")
            .def("&8&oThe player &4&n%enemy% &8&o died after popping &7&o(&e%pops%&7&o) &8totems!")
            .build()
    );
    public final ModuleSetting<Boolean> highlightOwn = general.add(BooleanSetting.create()
            .name("show-own")
            .description("Toggle showing your own pops in chat")
            .def(true)
            .build()
    );
    public final ModuleSetting<String> ownName = general.add(StringSetting.create()
            .name("own-pop-name")
            .description("Want to replace your name with in pop messages? (%player% for own name)")
            .def("&6&n%player%")
            .build()
    );

    private static final Map<String,Integer> totemPops = new HashMap<>();

    public TotemPops() {
        super("totem-pops", Categories.MISC, "Send messages when a player pops their totem");
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
            if (PlayerUtils.invalid())
                return;

            var p = PlayerUtils.player();
            final Entity ent = packet.getEntity(PlayerUtils.getWorld());
            final int status = packet.getStatus();

            if (ent == null) return;
            if (ent.getType() != EntityType.PLAYER) return;

            String name = ent.getDisplayName().getString();
            if (name.equals(p.getDisplayName().getString())) {
                if (highlightOwn.getVal()) {
                    name = StringUtils.color(ownName.getVal());
                } else {
                    return;
                }
            }

            if (status == EntityStatuses.USE_TOTEM_OF_UNDYING) {
                setPops(ent,getPops(ent) + 1);

                if (isEnabled()) {
                    ChatUtils.sendPrefixMessage(compilePopMsg(ent, name, enemyPops.getVal()));
                }
            }
            else if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
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
                .replaceAll("%enemy%", entName)
                .replaceAll("%player%",mc.getSession().getUsername());
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
