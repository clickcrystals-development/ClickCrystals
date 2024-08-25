package io.github.itzispyder.clickcrystals.mixins;


import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.ElytraSwitch;
import io.github.itzispyder.clickcrystals.modules.modules.misc.NoInteractions;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.item.Items.ELYTRA;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity implements Global {

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.BEFORE))
    public void swapToElytra(CallbackInfo callbackInfo) {
        if (PlayerUtils.invalid() || !Module.isEnabled(ElytraSwitch.class))
            return;

        var p = PlayerUtils.player();
        if (!p.isOnGround() && !p.isFallFlying() && !p.isTouchingWater() && !p.hasStatusEffect(StatusEffects.LEVITATION)) {
            HotbarUtils.search(ELYTRA);
            if (HotbarUtils.isHolding(ELYTRA)) {
                InteractionUtils.inputUse();
            }
        }
    }
    @Inject(method = "openEditSignScreen", at = @At("HEAD"), cancellable = true)
    private void cancelOpenContainer(SignBlockEntity sign, boolean front, CallbackInfo ci) {
        NoInteractions noInteractions = Module.get(NoInteractions.class);
        if (noInteractions.isEnabled() && !noInteractions.allowSign.getVal()) {
            ci.cancel();
        }
    }
}