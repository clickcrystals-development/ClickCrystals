package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.events.events.client.ChatCommandEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.client.ChatSendEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener implements Global {

    @Shadow
    public abstract void sendChat(String content);
    @Unique
    private static boolean ignoreChatMessage = false;

    @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String content, CallbackInfo ci) {
        String prefix = ClickCrystals.commandPrefix.getKeyName();
        prefix = prefix.equals("NONE") ? "'" : prefix;

        if (content.startsWith(prefix)) {
            if (content.startsWith(prefix + prefix)) {
                return;
            }

            try {
                Command.dispatch(content.substring(prefix.length()));
            }
            catch (CommandSyntaxException ex) {
                Command.error(ex.getMessage());
            }
            ci.cancel();
            return;
        }

        if (!ignoreChatMessage) {
            ChatSendEvent event = new ChatSendEvent(content);
            system.eventBus.pass(event);

            if (!event.isCancelled()) {
                ignoreChatMessage = true;
                sendChat(event.getMessage());
                ignoreChatMessage = false;
            }

            ChatReceiveEvent.lock();
            ci.cancel();
        }
    }

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    public void sendCommand(String command, CallbackInfo ci) {
        ChatCommandEvent event = new ChatCommandEvent(command);
        system.eventBus.pass(event);
        if (event.isCancelled()) ci.cancel();
    }
}