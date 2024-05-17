package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.ChatReceiveEvent;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements Global {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",at = @At("HEAD"), cancellable = true)
    public void addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new ChatReceiveEvent(message.getString()));
        ChatReceiveEvent.unlock();
    }

    @Inject(method = "logChatMessage", at = @At("HEAD"), cancellable = true)
    public void logChatMessage(ChatHudLine message, CallbackInfo ci) {
        String msg = StringUtils.decolor(message.content().getString());
        if (msg.toLowerCase().contains(modId)) {
            ci.cancel();
            system.logger.log("SYSTEM-CHAT", msg);
        }
    }
}
