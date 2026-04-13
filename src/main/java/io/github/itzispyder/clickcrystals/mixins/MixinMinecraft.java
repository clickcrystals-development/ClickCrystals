package io.github.itzispyder.clickcrystals.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameLeaveEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SelfGlow;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements Global {

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void set(Screen screen, CallbackInfo ci) {
        SetScreenEvent event = new SetScreenEvent(screen);
        system.eventBus.pass(event);

        if (event.isCancelled()) {
            ci.cancel();
            AccessorMinecraftClient mc = (AccessorMinecraftClient) this;
            mc.invokeSetScreen(null);
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD") ,cancellable = true)
    private void attack(CallbackInfoReturnable<Boolean> cir) {
        AccessorMinecraftClient amc = (AccessorMinecraftClient) this;
        PlayerAttackEntityEvent evt = new PlayerAttackEntityEvent(mc.player, amc.accessTargetedEntity(), mc.hitResult);
        system.eventBus.passWithCallbackInfo(cir, evt);
    }

    @Inject(method = "destroy", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        system.onClientStopping();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("HEAD"))
    public void disconnect(Screen disconnectionScreen, boolean transferring, boolean bl, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @Inject(method = "disconnectFromWorld(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"))
    public void disconnect(Component reasonText, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @ModifyReturnValue(method = "shouldEntityAppearGlowing", at = @At("RETURN"))
    private boolean hasOutline(boolean original, Entity entity) {
        return Module.isEnabled(SelfGlow.class)
                ? entity == PlayerUtils.player()
                : original;
    }
}