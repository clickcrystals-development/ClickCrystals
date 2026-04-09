package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.BlockOutline;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LevelRenderer.class)
public class MixinWorldRenderer implements Global {

    @ModifyArgs(method = "renderHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;colorFromFloat(FFFF)I"))
    public void setOutlineColor(Args args) {
        BlockOutline bo = Module.get(BlockOutline.class);

        if (bo != null && bo.isEnabled()) {
            float m = 1F / 0xFF;
            float a = 1.0F;
            float r = bo.red.getVal().floatValue() * m;
            float g = bo.green.getVal().floatValue() * m;
            float b = bo.blue.getVal().floatValue() * m;

            args.set(0, a);
            args.set(1, r);
            args.set(2, g);
            args.set(3, b);
        }
    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    public void render(GraphicsResourceAllocator allocator, DeltaTracker tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f matrix4f, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci) {
        RenderWorldEvent event = new RenderWorldEvent(mc.gameRenderer, positionMatrix, projectionMatrix, tickCounter);
        system.eventBus.pass(event);
    }
}
