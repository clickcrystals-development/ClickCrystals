package io.github.itzispyder.clickcrystals.events.events.networking;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.network.protocol.Packet;

public class PacketSentEvent extends Event {

    private final Packet<?> packet;

    public PacketSentEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
