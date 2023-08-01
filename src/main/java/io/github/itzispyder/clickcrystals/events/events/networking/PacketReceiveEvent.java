package io.github.itzispyder.clickcrystals.events.events.networking;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.network.packet.Packet;

/**
 * Called when the client receives a packet for the server
 */
public class PacketReceiveEvent extends Event implements Cancellable {

    private final Packet<?> packet;
    private boolean cancelled;

    /**
     * Constructs a new client receive packet event
     * @param packet packet received
     */
    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
        this.cancelled = false;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
