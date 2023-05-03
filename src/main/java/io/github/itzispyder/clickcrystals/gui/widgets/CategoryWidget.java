package io.github.itzispyder.clickcrystals.gui.widgets;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class CategoryWidget extends CCWidget {

    public CategoryWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    public void onRender(MatrixStack matrices, int x, int y, float d) {
        DrawableHelper.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x40FFFFFF);
        DrawableHelper.drawBorder(matrices, getX(), getY(), getWidth(), getHeight(), 0xFFFFFFFF);
        DrawableHelper.drawCenteredTextWithShadow(matrices, mc.textRenderer, getMessage(), getX() + (getWidth() / 2), getY() + 5, 0xFFFFFFFF);
        DrawableHelper.drawHorizontalLine(matrices, getX(), getX() + getWidth() - 1, getY() + 15, 0xFFFFFFFF);
    }

    public void addModule(Module module) {

    }
}
