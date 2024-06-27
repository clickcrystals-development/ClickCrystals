package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameLeaveEvent;
import io.github.itzispyder.clickcrystals.interfaces.MinecraftClientAccessor;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SelfGlow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements MinecraftClientAccessor, Global {

    @Shadow public abstract void setScreen(@Nullable Screen screen);
    @Shadow protected abstract boolean doAttack();
    @Shadow protected abstract void doItemUse();

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void set(Screen screen, CallbackInfo ci) {
        SetScreenEvent event = new SetScreenEvent(screen);
        system.eventBus.pass(event);

        if (event.isCancelled()) {
            ci.cancel();
            setScreen(null);
        }
    }

    @Inject(method = "stop", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        system.onClientStopping();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    public void disconnect(Screen screen, CallbackInfo ci) {
        system.eventBus.pass(new GameLeaveEvent());
    }

    @Override
    public void inputAttack() {
        this.doAttack();
    }

    @Override
    public void inputUse() {
        this.doItemUse();
    }

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity instanceof ClientPlayerEntity && Module.isEnabled(SelfGlow.class))
            ci.setReturnValue(true);
    }
}
