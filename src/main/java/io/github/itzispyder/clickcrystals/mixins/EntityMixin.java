package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for entity class
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isInvisibleTo", at = @At("RETURN"))
    public void isInvisibleTo(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {

    }
}
