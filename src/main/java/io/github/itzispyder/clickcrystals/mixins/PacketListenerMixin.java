package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.events.events.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Client connection mixin for sending and handling packets
 */
@Mixin(ClientConnection.class)
public abstract class PacketListenerMixin {

    /**
     * When a packet is sent
     * @param packet packet sent
     * @param ci callback info
     */
    @Inject(method = "send*", at = @At("HEAD"), cancellable = true)
    public void onPacketSend(Packet<?> packet, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(packet);
        system.eventBus.pass(event);
        if (event.isCancelled()) ci.cancel();
    }

    /**
     * When a packet is read
     * @param packet packet read
     * @param listener packet listener
     * @param ci callback info
     */
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static void onPacketRead(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        system.eventBus.pass(event);
        if (event.isCancelled()) ci.cancel();
    }
}
