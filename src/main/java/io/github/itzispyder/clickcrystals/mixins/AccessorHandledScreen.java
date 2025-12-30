package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandledScreen.class)
public interface AccessorHandledScreen {

    @Invoker("isPointOverSlot")
    boolean isHovered(Slot slot, double mouseX, double mouseY);
}
