package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;

public class SoundOnDeath extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Double> distance = scGeneral.add(DoubleSetting.create()
            .max(15.0)
            .min(0.0)
            .decimalPlaces(1)
            .name("entity-range")
            .description("Distance for entity range.")
            .def(7.5)
            .build()
    );

    public final ModuleSetting<Double> pitch = scGeneral.add(DoubleSetting.create()
            .max(1.0)
            .min(0.0)
            .decimalPlaces(1)
            .name("sound-pitch")
            .description("Pitch of the death sound.")
            .def(1.0)
            .build()
    );

    public final ModuleSetting<Double> volume = scGeneral.add(DoubleSetting.create()
            .max(100.0)
            .min(0.0)
            .decimalPlaces(1)
            .name("sound-volume")
            .description("Volume of the death sound.")
            .def(10.0)
            .build()
    );

    public SoundOnDeath() {
        super("sound-on-death", Categories.MISC, "Plays a sound upon killing a player");
    }

    @EventHandler
    private void onReceivePacket(PacketReceiveEvent e) {
        if (e.getPacket() instanceof ClientboundEntityEventPacket packet && PlayerUtils.valid()) {
            LocalPlayer p = PlayerUtils.player();
            Entity ent = packet.getEntity(p.level());
            int status = packet.getEventId();
            boolean playerWithinRange = ent instanceof Player player && player.position().distanceTo(p.position()) < distance.getVal() && player != p;

            if (status == EntityEvent.DEATH && playerWithinRange) {
                this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, pitch.getVal().floatValue(), volume.getVal().floatValue());
            }
        }
    }

    private void playSound(SoundEvent soundEvent, float volume, float pitch) {
        SoundInstance sound = SimpleSoundInstance.forUI(soundEvent, volume, pitch);
        mc.getSoundManager().play(sound);
    }
}
