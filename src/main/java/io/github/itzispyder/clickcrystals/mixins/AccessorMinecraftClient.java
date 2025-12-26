package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface AccessorMinecraftClient {

    @Accessor("targetedEntity")
    Entity accessTargetedEntity();

    @Invoker("setScreen")
    void invokeSetScreen(Screen screen);

    @Invoker("doAttack")
    @SuppressWarnings("all")
    boolean inputAttack();

    @Invoker("doItemUse")
    void inputUse();
}
