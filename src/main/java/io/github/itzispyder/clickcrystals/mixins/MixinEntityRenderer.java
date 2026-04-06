package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.GlowingEntities;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> {

    @Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
    public void getLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        GlowingEntities ge = Module.get(GlowingEntities.class);

        if (ge.isEnabled()) {
            double light = ge.lightLevel.getVal();
            cir.setReturnValue((int) light);
        }
    }
}
