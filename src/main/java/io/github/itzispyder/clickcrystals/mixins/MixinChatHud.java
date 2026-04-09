package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public abstract class MixinChatHud implements Global {

    @Inject(method = "addMessage",at = @At("HEAD"), cancellable = true)
    public void addMessage(Component contents, MessageSignature signature, GuiMessageSource source, GuiMessageTag tag, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new ChatReceiveEvent(contents.getString()));
        ChatReceiveEvent.unlock();
    }

    @Inject(method = "logChatMessage", at = @At("HEAD"), cancellable = true)
    public void logChatMessage(GuiMessage message, CallbackInfo ci) {
        String msg = StringUtils.decolor(message.content().getString());
        if (msg.toLowerCase().contains(modId)) {
            ci.cancel();
            system.logger.log("SYSTEM-CHAT", msg);
        }
    }
}
