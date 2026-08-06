package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface AccessorMinecraftClient {

    @Accessor("crosshairPickEntity")
    Entity accessTargetedEntity();

    @Invoker("startAttack")
    @SuppressWarnings("all")
    boolean inputAttack();

    @Invoker("startUseItem")
    void inputUse();
}
