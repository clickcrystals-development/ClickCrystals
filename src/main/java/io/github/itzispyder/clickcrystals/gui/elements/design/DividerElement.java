package io.github.itzispyder.clickcrystals.gui.elements.design;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class DividerElement extends GuiElement {

    private final String text;
    private final float textScale;

    public DividerElement(String text, int x, int y, int width, int height, float textScale) {
        super(x, y, width, height);
        this.text = text;
        this.textScale = textScale;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int nameWidth = (int)(mc.textRenderer.getWidth(text) * textScale);
        int nameHeight = (int)(10 * textScale);
        int textMargin = (int)(width * 0.05);

        RenderUtils.drawHorizontalLine(context, x, y + nameHeight / 2, textMargin, 1, 0xFF555555);
        RenderUtils.drawText(context, "§7" + text, x + textMargin + 3, y + nameHeight / 4, textScale, true);
        RenderUtils.drawHorizontalLine(context, x + textMargin + nameWidth + 6, y + nameHeight / 2, width - (textMargin + nameWidth + 16), 1, 0xFF555555);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public String getText() {
        return text;
    }

    public float getTextScale() {
        return textScale;
    }
}
