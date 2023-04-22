package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;

/**
 * NoServerParticles module
 */
public class NoServerParticles extends Module implements Listener {

    public NoServerParticles() {
        super("NoServerParticles", Categories.OPTIMIZATION,"Prevents servers from sending you particle packets, stopping malicious particle crashes.");
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
    private void onReceiveParticle(PacketReceiveEvent e) {
        if (e.getPacket() instanceof ParticleS2CPacket) {
            e.setCancelled(true);
        }
    }
}
