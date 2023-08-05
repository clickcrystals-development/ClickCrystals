package io.github.itzispyder.clickcrystals.events.events.networking;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.network.packet.Packet;

public class PacketSentEvent extends Event {

    private final Packet<?> packet;

    public PacketSentEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
