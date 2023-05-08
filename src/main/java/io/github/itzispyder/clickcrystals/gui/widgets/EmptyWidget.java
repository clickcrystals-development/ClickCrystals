package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.gui.Draggable;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class EmptyWidget extends CCWidget {

    private int color;

    public EmptyWidget(int x, int y, int width, int height, Text message, int color) {
        super(x, y, width, height, message);
        this.color = color;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), getColor());
        DrawableUtils.drawCenteredText(matrices, getMessage(), getX() + (getWidth() / 2), getY() + (int)(getHeight() * 0.33), true);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public <T extends Draggable> List<T> getDraggableChildren() {
        return null;
    }
}
