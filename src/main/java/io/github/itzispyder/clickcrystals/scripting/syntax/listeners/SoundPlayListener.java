package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.events.client.SoundPlayEvent;

@FunctionalInterface
public interface SoundPlayListener {

    void pass(SoundPlayEvent e);
}
