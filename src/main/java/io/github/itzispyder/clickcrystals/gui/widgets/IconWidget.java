package io.github.itzispyder.clickcrystals.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.gui.Draggable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class IconWidget extends CCWidget {

    private final Identifier texture;

    public IconWidget(int x, int y, int width, int height, Identifier texture) {
        super(x, y, width, height, Text.literal(""));
        this.texture = texture;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(matrices, getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }

    public Identifier getTexture() {
        return texture;
    }

    @Override
    public <T extends Draggable> List<T> getDraggableChildren() {
        return null;
    }
}
