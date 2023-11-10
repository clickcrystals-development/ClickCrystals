package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.ParticleReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;

public class ExplodeParticles extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> disableFireSmoke = scGeneral.add(BooleanSetting.create()
            .name("disable-black-fire-smoke")
            .description("Disables rendering of the black fire smoke particle.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> disablePoofCloud = scGeneral.add(BooleanSetting.create()
            .name("disable-white-poof-cloud")
            .description("Disables rendering of the white cloud poof particle.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> disablePotionParticles = scGeneral.add(BooleanSetting.create()
            .name("disable-potion-particles")
            .description("Disables potion effect particles from sources such as gaps or exp bottles.")
            .def(true)
            .build()
    );

    public ExplodeParticles() {
        super("explode-particles", Categories.LAG, "Turns off explosion particles for smoother crystal pvp!");
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
        ParticleType<?> p = e.getType();

        if (p == ParticleTypes.EXPLOSION || p == ParticleTypes.EXPLOSION_EMITTER) {
            e.cancel();
        } else if ((p == ParticleTypes.SMOKE || p == ParticleTypes.LARGE_SMOKE) && disableFireSmoke.getVal()) {
            e.cancel();
        } else if ((p == ParticleTypes.POOF || p == ParticleTypes.CLOUD) && disablePoofCloud.getVal()) {
            e.cancel();
        } else if ((p == ParticleTypes.EFFECT || p == ParticleTypes.ENTITY_EFFECT) && disablePotionParticles.getVal()) {
            e.cancel();
        }
    }
}
