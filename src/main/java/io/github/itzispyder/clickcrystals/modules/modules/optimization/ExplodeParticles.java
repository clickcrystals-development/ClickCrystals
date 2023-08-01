package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ParticleReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.particle.ParticleTypes;

public class ExplodeParticles extends Module implements Listener {

    public ExplodeParticles() {
        super("explode-particles", Categories.OPTIMIZATION, "Turns off explosion particles for smoother crystal pvp!");
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
    private void onParticleReceive(ParticleReceiveEvent e) {
        if (e.getType() == ParticleTypes.EXPLOSION || e.getType() == ParticleTypes.EXPLOSION_EMITTER) {
            e.setCancelled(true);
        }
    }
}
