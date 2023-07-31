package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;

public class AntiCrash extends Module implements Listener {

    private final SettingSection general = getGeneralSection();
    public final ModuleSetting<Integer> maxParticles = general.add(IntegerSetting.create()
            .max(10000)
            .min(10)
            .name("particle-limit")
            .description("The maximum amount of particles a packet can contain before being ignored.")
            .def(10000)
            .build()
    );

    public AntiCrash() {
        super("anti-crash", Categories.OPTIMIZATION, "Prevents servers from crashing your game by ignoring various packets.");
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
        if (e.getPacket() instanceof ParticleS2CPacket p && p.getCount() > maxParticles.getVal()) {
            e.setCancelled(true);
        } else if (e.getPacket() instanceof ExplosionS2CPacket) {
            ExplosionS2CPacket p = (ExplosionS2CPacket) e.getPacket();
            float v = Math.abs(p.getPlayerVelocityX()) + Math.abs(p.getPlayerVelocityY()) + Math.abs(p.getPlayerVelocityZ());
            int o = 30000000; // oob

            if (v > o || p.getAffectedBlocks().size() > 100000 || p.getRadius() > 1000
                    || Math.abs(p.getX()) > o || Math.abs(p.getY()) > o || Math.abs(p.getZ()) > o) {
                e.setCancelled(true);
            }
        }
    }
}
