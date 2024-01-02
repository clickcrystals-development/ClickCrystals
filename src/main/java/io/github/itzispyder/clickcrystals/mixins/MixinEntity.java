package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.SpectatorSight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void overrideIsInvisibleToPlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        SpectatorSight ss = Module.get(SpectatorSight.class);

        if (ss.isEnabled() && ss.canRender((Entity)(Object)this)) {
            cir.setReturnValue(false);
        }
    }
}
