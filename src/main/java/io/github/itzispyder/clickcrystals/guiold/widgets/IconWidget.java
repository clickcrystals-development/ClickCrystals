package io.github.itzispyder.clickcrystals.guiold.widgets;

import io.github.itzispyder.clickcrystals.guiold.Draggable;
import net.minecraft.client.gui.DrawContext;
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
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack m = context.getMatrices();

        context.drawTexture(texture, getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }

    public Identifier getTexture() {
        return texture;
    }

    @Override
    public <T extends Draggable> List<T> getDraggableChildren() {
        return null;
    }
}
