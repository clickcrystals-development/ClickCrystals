package io.github.itzispyder.clickcrystals.gui.display;

import io.github.itzispyder.clickcrystals.gui.DisplayableElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.util.math.MatrixStack;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class TextLabelElement extends DisplayableElement {

    private String text;

    public TextLabelElement(int x, int y, String text) {
        super(x, y, 40, 10);
        this.text = text;
    }

    @Override
    public void render(MatrixStack matrices, double mouseX, double mouseY) {
        super.render(matrices, mouseX, mouseY);

        DrawableUtils.drawCenteredText(matrices, text, getX() + (getWidth() / 2), getY() + (int)(getHeight() * 0.33), true);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.updateWidth();
    }

    public void updateWidth() {
        this.setWidth(mc.textRenderer.getWidth(text) + 10);
        this.setHeight(mc.textRenderer.fontHeight + 10);
    }

    @Override
    public boolean canDrag() {
        return false;
    }
}
