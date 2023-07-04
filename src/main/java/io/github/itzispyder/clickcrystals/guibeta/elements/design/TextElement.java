package io.github.itzispyder.clickcrystals.guibeta.elements.design;

import io.github.itzispyder.clickcrystals.guibeta.GuiElement;
import io.github.itzispyder.clickcrystals.guibeta.TextAlignment;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class TextElement extends GuiElement {

    public static final int MARGIN = 1;
    private String text;
    private TextAlignment alignment;
    private float scale;
    private int backgroundColor;

    public TextElement(String text, TextAlignment alignment, float scale, int x, int y, int backgroundColor) {
        super(x, y, 0, 0);
        this.text = text;
        this.alignment = alignment;
        this.scale = scale;
        this.width = (int)((mc.textRenderer.getWidth(text) + (2 * MARGIN)) * scale);
        this.height = (int)(10 * scale);
        this.backgroundColor = backgroundColor;
    }

    public TextElement(String text, TextAlignment alignment, float scale, int x, int y) {
        this(text, alignment, scale, x, y, 0x00000000);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.height = (int)(10 * scale);

        DrawableUtils.fill(context, x, y, width, height, backgroundColor);

        switch (alignment) {
            case CENTER -> DrawableUtils.drawCenteredText(context, text, x + width / 2, y + (int)(height * 0.33), scale, true);
            case RIGHT -> DrawableUtils.drawRightText(context, text, x + width - MARGIN, y + (int)(height * 0.33), scale, true);
            case LEFT -> DrawableUtils.drawText(context, text, x + MARGIN, y + (int)(height * 0.33), scale, true);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public String getText() {
        return text;
    }

    public TextAlignment getAlignment() {
        return alignment;
    }

    public float getScale() {
        return scale;
    }

    public void setText(String text) {
        this.text = text;
        this.width = (int)((mc.textRenderer.getWidth(text) + (2 * MARGIN)) * scale);
        this.height = (int)(10 * scale);
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
