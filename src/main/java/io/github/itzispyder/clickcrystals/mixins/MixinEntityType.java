package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.gui.misc.brushes.MobHeadBrush;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class MixinEntityType {

    @Inject(method = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;", at = @At("RETURN"))
    private static <T extends Entity> void register(String vanillaId, EntityType.Builder<T> builder, CallbackInfoReturnable<EntityType<T>> cir) {
        MobHeadBrush.init(cir.getReturnValue(), vanillaId);
    }
}