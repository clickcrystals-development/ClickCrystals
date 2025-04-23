package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.HealthTags;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.RenderOwnName;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer implements Global {

    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z", at = @At("RETURN"), cancellable = true)
    public void hasLabel(LivingEntity ent, double d, CallbackInfoReturnable<Boolean> cir) {
        if (Module.isEnabled(RenderOwnName.class) && mc.getCameraEntity() == ent) {
            cir.setReturnValue(MinecraftClient.isHudEnabled());
        }

        var health = HealthTags.get(HealthTags.class);

        if (health.isEnabled()) {
            cir.setReturnValue(true);
        }
    }
}
