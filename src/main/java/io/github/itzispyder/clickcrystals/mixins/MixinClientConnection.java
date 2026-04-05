package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.EntityDamageEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameJoinEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSentEvent;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class MixinClientConnection implements Global {

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
    public void onPacketSend(Packet<?> packet, @Nullable ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new PacketSendEvent(packet));
    }

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("TAIL"))
    public void onPacketSent(Packet<?> packet, @Nullable ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new PacketSentEvent(packet));
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private static void onPacketRead(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
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
