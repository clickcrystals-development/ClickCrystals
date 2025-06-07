package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class SuggestionElement extends GuiElement {

    private final Identifier texture;
    private final Consumer<SuggestionElement> clickAction;
    private final String title;

    public SuggestionElement(Identifier iconTex, String title, int x, int y, int size, Consumer<SuggestionElement> onClick) {
        super(x, y, size, size);
        this.texture = iconTex;
        this.clickAction = onClick;
        this.title = title;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int fill = isHovered(mouseX, mouseY) ? Shades.LIGHT_GRAY : Shades.GRAY;
        RenderUtils.fillRoundRect(context, x, y, width, height, 3, fill);
        RenderUtils.drawTexture(context, texture, x + 5, y + 5, width - 10, height - 10);

        int cx = x + width / 2 + 1;
        int cy = y + height + 5;
        RenderUtils.drawCenteredText(context, title, cx, cy, 0.7F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        clickAction.accept(this);
    }
}
