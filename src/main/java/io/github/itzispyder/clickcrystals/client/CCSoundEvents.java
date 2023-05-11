package io.github.itzispyder.clickcrystals.client;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static io.github.itzispyder.clickcrystals.ClickCrystals.MOD_ID;

public final class CCSoundEvents {

    public static void init() {
        register(VINEBOOM);
    }

    private static void register(SoundEvent soundEvent) {
        Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent);
    }

    public static final SoundEvent
            VINEBOOM = SoundEvent.of(new Identifier(MOD_ID, "vineboom"));

}
