package io.github.itzispyder.clickcrystals.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameLeaveEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SelfGlow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements Global {

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

    @Inject(method = "doAttack", at = @At("HEAD") ,cancellable = true)
    private void attack(CallbackInfoReturnable<Boolean> cir) {
        AccessorMinecraftClient amc = (AccessorMinecraftClient) this;
        PlayerAttackEntityEvent evt = new PlayerAttackEntityEvent(mc.player, amc.accessTargetedEntity(), mc.crosshairTarget);
        system.eventBus.passWithCallbackInfo(cir, evt);
    }

    @Inject(method = "stop", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        system.onClientStopping();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;ZZ)V", at = @At("HEAD"))
    public void disconnect(Screen disconnectionScreen, boolean transferring, boolean bl, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @Inject(method = "disconnect(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    public void disconnect(Text reasonText, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @ModifyReturnValue(method = "hasOutline", at = @At("RETURN"))
    private boolean hasOutline(boolean original, Entity entity) {
        if (mc == null) return original;
        SelfGlow selfGlow = Module.get(SelfGlow.class);
        return selfGlow != null ? selfGlow.isEnabled() && entity == mc.player : original;
    }
}
