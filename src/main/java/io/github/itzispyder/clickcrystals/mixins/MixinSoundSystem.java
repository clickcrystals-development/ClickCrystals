package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.SoundPlayEvent;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class MixinSoundSystem implements Global {

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    public void play(SoundInstance instance, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        system.eventBus.passWithCallbackInfo(cir, new SoundPlayEvent(instance));
    }

    @Inject(method = "playDelayed", at = @At("HEAD"), cancellable = true)
    public void play(SoundInstance sound, int delay, CallbackInfo ci) {
        system.eventBus.passWithCallbackInfo(ci, new SoundPlayEvent(sound));
    }
}
