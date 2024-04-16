package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.HealthTags;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer {

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void renderLabelIfPresent(AbstractClientPlayerEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, CallbackInfo ci) {
        var health = HealthTags.get(HealthTags.class);

        if (health.isEnabled()) {
            ci.cancel();
            health.render(matrices, entity, vertexConsumers);
        }
    }
}
