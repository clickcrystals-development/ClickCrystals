package io.github.itzispyder.clickcrystals.mixins;


import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.ElytraSwitch;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
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
        if (Module.isEnabled(ElytraSwitch.class)) {
            if (!mc.player.isOnGround() && !mc.player.isFallFlying() && !mc.player.isTouchingWater() && !mc.player.hasStatusEffect(StatusEffects.LEVITATION)) {
                HotbarUtils.search(ELYTRA);
                InteractionUtils.inputUse();
            }
        }
    }
}