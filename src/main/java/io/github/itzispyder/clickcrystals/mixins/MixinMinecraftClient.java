package io.github.itzispyder.clickcrystals.mixins;

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

    @Inject(method = "startAttack", at = @At("HEAD") ,cancellable = true)
    private void attack(CallbackInfoReturnable<Boolean> cir) {
        AccessorMinecraftClient amc = (AccessorMinecraftClient) this;
        PlayerAttackEntityEvent evt = new PlayerAttackEntityEvent(mc.player, amc.accessTargetedEntity(), mc.hitResult);
        system.eventBus.passWithCallbackInfo(cir, evt);
    }

    @Inject(method = "stop", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        system.onClientStopping();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("HEAD"))
    public void disconnect(Screen disconnectionScreen, boolean transferring, boolean bl, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V", at = @At("HEAD"))
    public void disconnect(Screen screen, boolean keepResourcePacks, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @Inject(method = "disconnectFromWorld", at = @At("HEAD"))
    public void disconnectFromWorld(Component message, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @Inject(method = "shouldEntityAppearGlowing", at = @At("RETURN"), cancellable = true)
    private void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Module.isEnabled(SelfGlow.class)
                ? entity == PlayerUtils.player()
                : cir.getReturnValue()
        );
    }
}
