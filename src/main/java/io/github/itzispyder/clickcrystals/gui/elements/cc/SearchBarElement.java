package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class SearchBarElement extends GuiElement {

    private String query;
    private float textScale;

    public SearchBarElement(int x, int y, int width, float textScale) {
        super(x, y, width, (int)(10 * textScale) + 4);
        this.textScale = textScale;

        ImageElement search = new ImageElement(TexturesIdentifiers.SEARCH, x + 2, y + 2, getTextHeight(), getTextHeight());
        this.addChild(search);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        String text = query;
        int textWidth = (int)(textScale * mc.textRenderer.getWidth(text));
        int textHeight = getTextHeight();

        while (textWidth > width - 4 - textHeight) {
            text = text.substring(1);
        }

        DrawableUtils.drawText(context, text, x + 2 + textHeight, y + (int)(height * 0.33), textScale, true);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public void onKey(int key) {

    }

    public int getTextHeight() {
        return (int)(10 * textScale);
    }
}
