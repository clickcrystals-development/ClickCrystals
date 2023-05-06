package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.events.events.ChatCommandEvent;
import io.github.itzispyder.clickcrystals.events.events.ChatSendEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String content, CallbackInfo ci) {
        final ChatSendEvent event = new ChatSendEvent(content);

        system.eventBus.pass(event);
        content = event.getMessage();
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    public void sendCommand(String command, CallbackInfoReturnable<Boolean> cir) {
        final ChatCommandEvent event = new ChatCommandEvent(command);

        system.eventBus.pass(event);
        command = event.getCommandLine();
        if (event.isCancelled()) cir.cancel();
    }
}
