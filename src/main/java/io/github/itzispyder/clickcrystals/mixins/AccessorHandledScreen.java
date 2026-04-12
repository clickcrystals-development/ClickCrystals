package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractContainerScreen.class)
public interface AccessorHandledScreen {

    @Invoker("isHovering")
    boolean isHovered(Slot slot, double mouseX, double mouseY);
}