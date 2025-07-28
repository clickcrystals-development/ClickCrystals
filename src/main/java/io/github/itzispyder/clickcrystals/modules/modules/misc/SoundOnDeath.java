package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

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
        if (e.getPacket() instanceof EntityStatusS2CPacket packet && PlayerUtils.valid()) {
            ClientPlayerEntity p = PlayerUtils.player();
            Entity ent = packet.getEntity(p.getWorld());
            int status = packet.getStatus();
            boolean playerWithinRange = ent instanceof PlayerEntity player && player.getPos().distanceTo(p.getPos()) < distance.getVal() && player != p;

            if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES && playerWithinRange) {
                this.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, pitch.getVal().floatValue(), volume.getVal().floatValue());
            }
        }
    }

    private void playSound(SoundEvent soundEvent, float volume, float pitch) {
        SoundInstance sound = PositionedSoundInstance.master(soundEvent, volume, pitch);
        mc.getSoundManager().play(sound);
    }
}
