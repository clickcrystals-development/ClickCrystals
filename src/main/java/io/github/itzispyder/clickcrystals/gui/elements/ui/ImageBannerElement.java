package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ImageBannerElement extends ImageElement {

    private final String title, subtitle;
    private float textScale;

    public ImageBannerElement(Identifier texture, int x, int y, int width, int height, String title, String subtitle, float textScale) {
        super(texture, x, y, width, height);
        this.title = title;
        this.subtitle = subtitle;
        this.textScale = textScale;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        super.onRender(context, mouseX, mouseY);
        DrawableUtils.drawCenteredText(context, title, x + width / 2, y + (int)(height * 0.20), textScale, true);
        DrawableUtils.drawCenteredText(context, subtitle, x + width / 2, y + (int)(height * 0.65), textScale / 2, true);
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setTextScale(float textScale) {
        this.textScale = textScale;
    }

    public float getTextScale() {
        return textScale;
    }
}
