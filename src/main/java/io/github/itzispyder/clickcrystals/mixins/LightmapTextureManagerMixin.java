package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.FullBright;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for lighting and render for lighting
 */
@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {

    /**
     * When the game gets the render brightness
     * @param type dimension type
     * @param lightLevel light level
     * @param cir returnable callback info
     */
    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void getBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
        Module fullBright = Module.get(FullBright.class);
        if (fullBright.isEnabled()) cir.setReturnValue(15.0F);
    }
}
