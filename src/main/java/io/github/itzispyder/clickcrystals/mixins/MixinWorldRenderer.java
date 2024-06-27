package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SelfGlow;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.BlockOutline;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @ModifyArgs(method = "drawBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawCuboidShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V"))
    public void setOutlineColor(Args args) {
        BlockOutline bo = Module.get(BlockOutline.class);

        if (bo != null && bo.isEnabled()) {
            double r = bo.red.getVal();
            double g = bo.green.getVal();
            double b = bo.blue.getVal();
            float rf = (float)(r / 255.0);
            float gf = (float)(g / 255.0);
            float bf = (float)(b / 255.0);

            args.set(6, rf);
            args.set(7, gf);
            args.set(8, bf);
        }
    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void onRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (!(entity instanceof ClientPlayerEntity && vertexConsumers instanceof OutlineVertexConsumerProvider outlineConsumers))
            return;

        SelfGlow selfGlow = Module.get(SelfGlow.class);

        if (selfGlow.isEnabled()) {
            SelfGlow.Color glowColor = selfGlow.glowColor.getVal();
            int[] rgba = glowColor.getRGBA();
            outlineConsumers.setColor(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
    }
}
