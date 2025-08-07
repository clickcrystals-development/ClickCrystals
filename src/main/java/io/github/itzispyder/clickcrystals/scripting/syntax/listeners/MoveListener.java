package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@FunctionalInterface
public interface MoveListener {

    void pass(PlayerMoveC2SPacket e);
}
