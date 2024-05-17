package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.HealthTags;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.GlowingEntities;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Inject(method = "getBlockLight", at = @At("RETURN"), cancellable = true)
    public <T extends Entity> void getLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        GlowingEntities ge = Module.get(GlowingEntities.class);

        if (ge.isEnabled()) {
            double light = ge.lightLevel.getVal();
            cir.setReturnValue((int) light);
        }
    }

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        var health = HealthTags.get(HealthTags.class);

        if (health.isEnabled() && entity instanceof LivingEntity liv) {
            ci.cancel();
            health.render(matrices, liv, vertexConsumers);
        }
    }

    @Inject(method = "hasLabel", at = @At("RETURN"), cancellable = true)
    public <T extends Entity> void hasLabel(T entity, CallbackInfoReturnable<Boolean> cir) {
        var health = HealthTags.get(HealthTags.class);

        if (health.isEnabled() && entity instanceof LivingEntity) {
            cir.setReturnValue(true);
        }
    }
}
