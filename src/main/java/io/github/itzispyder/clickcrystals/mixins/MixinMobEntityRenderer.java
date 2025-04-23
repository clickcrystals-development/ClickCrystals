package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.HealthTags;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntityRenderer.class)
public abstract class MixinMobEntityRenderer {

    @Inject(method = "hasLabel(Lnet/minecraft/entity/mob/MobEntity;D)Z", at = @At("RETURN"), cancellable = true)
    public <T extends MobEntity> void hasLabel(T mobEntity, double d, CallbackInfoReturnable<Boolean> cir) {
        var health = HealthTags.get(HealthTags.class);

        if (health.isEnabled()) {
            cir.setReturnValue(true);
        }
    }
}
