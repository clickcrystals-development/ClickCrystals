package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;

public class NoScoreboard extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> cancelPackets = scGeneral.add(BooleanSetting.create()
            .name("cancel-scoreboard-packets")
            .description("Enabling this could potentially cause objective syncing errors.")
            .def(false)
            .build()
    );

    public NoScoreboard() {
        super("no-scoreboard", Categories.RENDER, "Disables the scoreboard sidebar display render");
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
    private void onReceivePacket(PacketReceiveEvent e) {
        if (cancelPackets.getVal()) {
            Packet<?> p = e.getPacket();
            if (p instanceof ScoreboardPlayerUpdateS2CPacket || p instanceof ScoreboardDisplayS2CPacket || p instanceof ScoreboardObjectiveUpdateS2CPacket) {
                e.setCancelled(true);
            }
        }
    }
}
