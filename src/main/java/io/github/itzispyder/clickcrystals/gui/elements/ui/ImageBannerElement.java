package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ImageBannerElement extends ImageElement {

    private String title, subtitle;

    public ImageBannerElement(Identifier texture, int x, int y, int width, int height, String title, String subtitle) {
        super(texture, x, y, width, height);
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        super.onRender(context, mouseX, mouseY);
        DrawableUtils.drawText(context, title, x + width / 2, y + (int)(height * 0.33), 2.0F, true);
        DrawableUtils.drawText(context, subtitle, x + width / 2, y + (int)(height * 0.75), 1.0F, true);
    }
}
