package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class HyperLinkElement extends GuiElement {

    private final String url, name;
    private final float textScale;

    public HyperLinkElement(int x, int y, String url, String name, float textScale) {
        super(x, y, 0, 0);
        this.url = url;
        this.name = name;
        this.textScale = textScale;

        this.width = (int)(mc.textRenderer.getWidth(name) * textScale);
        this.height = (int)(textScale * 10);
    }

    public HyperLinkElement(int x, int y, String url, float textScale) {
        this(x, y, url, url, textScale);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int color = isHovered(mouseX, mouseY) ? 0xFF55FFFF : 0xFF00AAAA;
        RenderUtils.drawText(context, "§3" + name, x, y, textScale, false);
        RenderUtils.drawHorLine(context, x, y + height + 1, width, color);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onClick(double mouseX, double mouseY, int button) {
        system.openUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
