package io.github.itzispyder.clickcrystals.mixins;


import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.ElytraSwitch;
import io.github.itzispyder.clickcrystals.modules.modules.misc.NoInteractions;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.item.Items.ELYTRA;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer implements Global {

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z", shift = At.Shift.BEFORE))
    public void swapToElytra(CallbackInfo callbackInfo) {
        if (PlayerUtils.invalid() || !Module.isEnabled(ElytraSwitch.class))
            return;

        var p = PlayerUtils.player();
        if (!p.onGround() && !p.isFallFlying() && !p.isInWater() && !p.hasEffect(MobEffects.LEVITATION)) {
            HotbarUtils.search(ELYTRA);
            if (HotbarUtils.isHolding(ELYTRA)) {
                InteractionUtils.inputUse();
            }
        }
    }
    @Inject(method = "openTextEdit", at = @At("HEAD"), cancellable = true)
    private void cancelOpenContainer(SignBlockEntity sign, boolean front, CallbackInfo ci) {
        NoInteractions noInteractions = Module.get(NoInteractions.class);
        if (noInteractions.isEnabled() && !noInteractions.allowSign.getVal()) {
            ci.cancel();
        }
    }
}