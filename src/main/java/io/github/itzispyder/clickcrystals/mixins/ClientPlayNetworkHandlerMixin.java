package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.itzispyder.clickcrystals.commands.CustomCommand;
import io.github.itzispyder.clickcrystals.events.events.ChatCommandEvent;
import io.github.itzispyder.clickcrystals.events.events.ChatSendEvent;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String content, CallbackInfo ci) {
        if (content.startsWith(CustomCommand.PREFIX)) {
            try {
                CustomCommand.dispatch(content.substring(CustomCommand.PREFIX.length()));
            }
            catch (CommandSyntaxException ex) {
                ChatUtils.sendPrefixMessage(StringUtils.color("&c" + ex.getMessage()));
            }
            ci.cancel();
        }

        ChatSendEvent event = new ChatSendEvent(content);
        system.eventBus.pass(event);
        content = event.getMessage();
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    public void sendCommand(String command, CallbackInfo ci) {
        ChatCommandEvent event = new ChatCommandEvent(command);
        system.eventBus.pass(event);
        command = event.getCommandLine();
        if (event.isCancelled()) ci.cancel();
    }
}
