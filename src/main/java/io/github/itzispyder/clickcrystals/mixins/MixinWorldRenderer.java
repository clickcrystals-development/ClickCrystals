package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.BlockOverlay;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @ModifyArgs(method = "drawBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawCuboidShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V"))
    public void setOutlineColor(Args args) {
        BlockOverlay blockOverlay = Module.get(BlockOverlay.class);

        if (blockOverlay != null) {
            // Retrieve color values from BlockOverlay module settings
            double red = blockOverlay.red.getVal();
            double greenValue = blockOverlay.green.getVal(); // Renamed to avoid conflict
            double blue = blockOverlay.blue.getVal();

            // Convert color values to the range [0.0, 1.0]
            float redFloat = (float) (red / 255.0);
            float greenFloat = (float) (greenValue / 255.0); // Use the renamed variable
            float blueFloat = (float) (blue / 255.0);

            // Set the color values in the modify args
            args.set(6, redFloat);
            args.set(7, greenFloat);
            args.set(8, blueFloat);
        }
    }
}
