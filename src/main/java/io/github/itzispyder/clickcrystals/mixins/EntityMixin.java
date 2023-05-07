package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TrueSight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for entity class
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract int getId();

    @Inject(method = "isInvisibleTo", at = @At("RETURN"), cancellable = true)
    public void isInvisibleTo(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        final Module spectatorsSight = Module.get(TrueSight.class);

        if (spectatorsSight.isEnabled()) cir.setReturnValue(false);
    }
}
