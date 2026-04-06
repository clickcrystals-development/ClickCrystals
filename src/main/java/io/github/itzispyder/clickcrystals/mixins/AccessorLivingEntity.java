package io.github.itzispyder.clickcrystals.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public interface AccessorLivingEntity {

    @Accessor("activeEffects")
    Map<Holder<MobEffect>, MobEffectInstance> accessActiveStatusEffects();
}
