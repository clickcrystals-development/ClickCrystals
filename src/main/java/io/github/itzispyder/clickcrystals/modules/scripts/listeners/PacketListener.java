package io.github.itzispyder.clickcrystals.modules.scripts.listeners;

import net.minecraft.network.packet.Packet;

@FunctionalInterface
public interface PacketListener {

    void pass(Packet<?> packet);
}
