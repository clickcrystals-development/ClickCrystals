package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.BlockOutline;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer implements Global {

    @ModifyArg(
            method = "submitHitOutline",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitShapeOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/client/renderer/rendertype/RenderType;IFZ)V",
                    ordinal = 3
            ),
            index = 3
    )
    private int setOutlineColor(int originalColor) {
        BlockOutline blockOutline = Module.get(BlockOutline.class);
        return blockOutline.isEnabled() ? blockOutline.color.getVal().getHexOpaque() : originalColor;
    }

    @Inject(method = "submitFeatures", at = @At("TAIL"))
    private void submitClickCrystalsFeatures(LevelRenderState renderState, SubmitNodeCollector submitNodeCollector, boolean renderOutline, CallbackInfo ci) {
        PoseStack pose = new PoseStack();
        DeltaTracker tickDelta = ((AccessorMinecraft) mc).tickDelta();
        RenderWorldEvent event = new RenderWorldEvent(pose, renderState, tickDelta, submitNodeCollector);
        system.eventBus.pass(event);
    }
}
