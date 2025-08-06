package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.sound.SoundInstance;

public class SoundPlayEvent extends Event implements Cancellable {

    private final SoundInstance sound;
    private boolean cancelled;

    public SoundPlayEvent(SoundInstance sound) {
        this.sound = sound;
    }

    public SoundInstance getSound() {
        return sound;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
