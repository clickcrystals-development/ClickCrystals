package io.github.itzispyder.clickcrystals.gui.misc.callbacks;

import net.minecraft.client.gui.GuiGraphicsExtractor;

@FunctionalInterface
public interface ScreenRenderCallback {

    void handleScreen(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta);
}
