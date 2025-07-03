package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SelfGlow;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.BlockOutline;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements Global {

    @ModifyArgs(method = "drawBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDI)V"))
    public void setOutlineColor(Args args) {
        BlockOutline bo = Module.get(BlockOutline.class);

        if (bo != null && bo.isEnabled()) {
            int r = bo.red.getVal().intValue();
            int g = bo.green.getVal().intValue();
            int b = bo.blue.getVal().intValue();

            args.set(6, new Color(r, g, b).getRGB());
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

    @Inject(method = "render", at = @At("TAIL"))
    public void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f projectionMatrix, GpuBufferSlice fog, Vector4f fogColor, boolean shouldRenderSky, CallbackInfo ci) {
        RenderWorldEvent event = new RenderWorldEvent(mc.gameRenderer, positionMatrix, projectionMatrix, tickCounter);
        system.eventBus.pass(event);
    }
}
