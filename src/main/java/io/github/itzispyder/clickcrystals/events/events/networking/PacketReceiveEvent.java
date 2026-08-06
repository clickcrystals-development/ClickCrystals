package io.github.itzispyder.clickcrystals.events.events.networking;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class PacketReceiveEvent extends Event implements Cancellable {

    private final Packet<?> packet;
    private final PacketListener listener;
    private boolean cancelled;

    public PacketReceiveEvent(Packet<?> packet, PacketListener listener) {
        this.packet = packet;
        this.listener = listener;
        this.cancelled = false;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public PacketListener getListener() {
        return listener;
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
