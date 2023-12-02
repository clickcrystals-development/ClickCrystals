package io.github.itzispyder.clickcrystals.gui_beta.elements.interactive;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class HyperLinkElement extends GuiElement {

    private final String url, name, message;
    private final float textScale;

    public HyperLinkElement(int x, int y, String url, String name, float textScale, String message) {
        super(x, y, 0, 0);
        this.url = url;
        this.name = name;
        this.textScale = textScale;
        this.message = message;

        this.width = (int)(mc.textRenderer.getWidth(name) * textScale);
        this.height = (int)(textScale * 10);
    }

    public HyperLinkElement(int x, int y, String url, float textScale, String message) {
        this(x, y, url, url, textScale, message);
    }

    @Override
    public void onRender(MatrixStack context, int mouseX, int mouseY) {
        int color = isHovered(mouseX, mouseY) ? 0xFF55FFFF : 0xFF00AAAA;
        RenderUtils.drawText(context, "§3" + name, x, y, textScale, false);
        RenderUtils.drawHorizontalLine(context, x, y + height + 1, width, 1, color);
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
