package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class PacketSenderMixin {

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    public void callEvent(Packet<?> packet, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(packet);
        system.eventBus.pass(event);
        if (event.isCancelled()) ci.cancel();
    }
}
