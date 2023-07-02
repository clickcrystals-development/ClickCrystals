package io.github.itzispyder.clickcrystals.guibeta.elements;

import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class BannerElement extends BoxElement {

    private String text;
    private float textScale;

    public BannerElement(String text, int x, int y, int width, int height, int color) {
        this(text, 1.0F, x, y, width, height, color);
    }

    public BannerElement(String text, float textScale, int x, int y, int width, int height, int color) {
        super(x, y, width, height, color);
        this.text = text;
        this.textScale = textScale;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        super.onRender(context, mouseX, mouseY);
        DrawableUtils.drawCenteredText(context, text, x + (width / 2), y + (int)(height * 0.33), false);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTextScale() {
        return textScale;
    }

    public void setTextScale(float textScale) {
        this.textScale = textScale;
    }
}
