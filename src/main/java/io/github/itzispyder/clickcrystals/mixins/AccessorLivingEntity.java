package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(LivingEntity.class)
public interface AccessorLivingEntity {

    @Accessor("activeStatusEffects")
    Map<RegistryEntry<StatusEffect>, StatusEffectInstance> accessActiveStatusEffects();
}
