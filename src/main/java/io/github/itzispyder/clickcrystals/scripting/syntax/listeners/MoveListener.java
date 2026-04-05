package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;


import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

@FunctionalInterface
public interface MoveListener {

    void pass(ServerboundMovePlayerPacket e);
}
