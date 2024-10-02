package io.github.itzispyder.clickcrystals.interfaces;

import net.minecraft.screen.slot.Slot;

public interface HandledScreenAccessor {

    boolean isHovered(Slot slot, double mouseX, double mouseY);
}
