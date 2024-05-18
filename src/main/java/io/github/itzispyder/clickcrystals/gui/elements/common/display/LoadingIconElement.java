package io.github.itzispyder.clickcrystals.gui.elements.common.display;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.RotationAxis;

public class LoadingIconElement extends GuiElement {

    private final Animator animator = new Animator(1000);

    public LoadingIconElement(int x, int y, int size) {
        super(x, y, size, size);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int x = this.x - this.width / 2;
        int y = this.y - this.height / 2;

        context.getMatrices().push();
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(360 * (float)animator.getProgress()), this.x, this.y, 0);
        RenderUtils.drawTexture(context, Tex.Icons.LOADING, x, y, width, height);
        context.getMatrices().pop();
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }
}
