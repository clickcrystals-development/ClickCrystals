package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import net.minecraft.network.packet.Packet;

@FunctionalInterface
public interface PacketListener {

    void pass(Packet<?> packet);
}
