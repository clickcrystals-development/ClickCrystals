package io.github.itzispyder.clickcrystals.gui.misc.callbacks;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface ScreenRenderCallback {

    void handleScreen(GuiGraphics context, int mouseX, int mouseY, float delta);
}
