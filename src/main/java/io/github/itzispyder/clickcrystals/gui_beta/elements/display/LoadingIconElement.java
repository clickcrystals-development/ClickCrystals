package io.github.itzispyder.clickcrystals.gui_beta.elements.display;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Rotation;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadingIconElement extends GuiElement {

    private final AtomicInteger rotation = new AtomicInteger(0);

    public LoadingIconElement(int x, int y, int size) {
        super(x, y, size, size);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        matrices.push();
        matrices.translate(x + width / 2.0F, y + height / 2.0F, 0);
        matrices.multiply(Rotation.of(rotation.get(), 0, 0, 1).getMatrix4f());
        RenderUtils.drawTexture(matrices, Tex.Icons.LOADING, x, y, width, height);
        matrices.pop();
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }
}
