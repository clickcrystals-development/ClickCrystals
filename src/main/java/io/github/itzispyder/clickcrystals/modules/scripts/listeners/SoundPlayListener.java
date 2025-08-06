package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import io.github.itzispyder.clickcrystals.events.events.client.SoundPlayEvent;

@FunctionalInterface
public interface SoundPlayListener {

    void pass(SoundPlayEvent e);
}
