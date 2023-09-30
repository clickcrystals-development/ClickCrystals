package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class HyperLinkElement extends GuiElement {

    private final String url, name, message;
    private final TextElement textElement;
    private final float textScale;

    public HyperLinkElement(int x, int y, String url, String name, float textScale, String message) {
        super(x, y, 0, 0);
        this.url = url;
        this.name = name;
        this.textScale = textScale;
        this.message = message;

        TextElement hyperlink = new TextElement("§3" + name, TextAlignment.LEFT, textScale, x, y);
        hyperlink.setBackgroundColor(0x0400AAAA);
        this.addChild(hyperlink);
        this.width = hyperlink.width;
        this.height = hyperlink.height;
        this.textElement = hyperlink;
    }

    public HyperLinkElement(int x, int y, String url, float textScale, String message) {
        this(x, y, url, url, textScale, message);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int color = isHovered(mouseX, mouseY) ? 0xFF55FFFF : 0xFF00AAAA;
        RenderUtils.drawHorizontalLine(context, x, textElement.y + textElement.height + 1, (int)(mc.textRenderer.getWidth(name) * textScale), 1, color);
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

    public String getMessage() {
        return message;
    }
}
