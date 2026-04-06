package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.EntityDamageEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameJoinEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSentEvent;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class MixinConnection implements Global {

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onPacketSend(Packet<?> packet, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new PacketSendEvent(packet));
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("TAIL"))
    public void onPacketSent(Packet<?> packet, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new PacketSentEvent(packet));
    }

    @Inject(method = "genericsFtw", at = @At("HEAD"), cancellable = true)
    private static void onPacketRead(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (PlayerUtils.invalid())
            return;

        if (packet instanceof ClientboundLoginFinishedPacket) {
            system.eventBus.pass(new GameJoinEvent());
        }
        if (packet instanceof ClientboundDamageEventPacket p) {
            system.eventBus.pass(new EntityDamageEvent(p));
        }
        system.eventBus.passWithCallbackInfo(ci, new PacketReceiveEvent(packet));
    }
}