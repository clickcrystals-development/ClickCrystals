package io.github.itzispyder.clickcrystals.interfaces;

import net.minecraft.screen.slot.Slot;

public interface AccessorHandledScreen {

    boolean isHovered(Slot slot, double mouseX, double mouseY);
}
