package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.network.Packet;

/**
 * Called when the client sends a packet to the server
 */
public class PacketSendEvent extends Event implements Cancellable {

    private final Packet<?> packet;
    private boolean cancelled;

    /**
     * Constructs a new client send packet event
     * @param packet packet sent
     */
    public PacketSendEvent(Packet<?> packet) {
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
