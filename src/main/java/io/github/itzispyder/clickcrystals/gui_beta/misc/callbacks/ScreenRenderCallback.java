package io.github.itzispyder.clickcrystals.gui_beta.misc.callbacks;

import net.minecraft.client.util.math.MatrixStack;

@FunctionalInterface
public interface ScreenRenderCallback {

    void handleScreen(MatrixStack context, int mouseX, int mouseY, float delta);
}
