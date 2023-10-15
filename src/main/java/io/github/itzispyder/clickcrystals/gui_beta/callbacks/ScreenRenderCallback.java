package io.github.itzispyder.clickcrystals.gui_beta.callbacks;

import net.minecraft.client.gui.DrawContext;

@FunctionalInterface
public interface ScreenRenderCallback {

    void handleScreen(DrawContext context, int mouseX, int mouseY, float delta);
}
