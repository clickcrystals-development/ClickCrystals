package io.github.itzispyder.clickcrystals.guiold.display;

import io.github.itzispyder.clickcrystals.guiold.DisplayableElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class TextLabelElement extends DisplayableElement {

    private String text;

    public TextLabelElement(int x, int y, String text) {
        super(x, y, 40, 10);
        this.text = text;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);

        DrawableUtils.drawCenteredText(context, text, getX() + (getWidth() / 2), getY() + (int)(getHeight() * 0.33), true);
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
