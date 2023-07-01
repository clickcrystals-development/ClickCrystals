package io.github.itzispyder.clickcrystals.gui.display;

import io.github.itzispyder.clickcrystals.gui.DisplayableElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;

public class WindowContainerElement extends DisplayableElement {

    private String title, description;
    private int lineWrapWidth;

    public WindowContainerElement(int x, int y, int width, int height, String title, String description, int lineWrapWidth) {
        super(x, y, width, height);
        this.title = title;
        this.description = description;
        this.lineWrapWidth = lineWrapWidth;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        if (!isVisible()) return;

        super.render(context, mouseX, mouseY);

        DrawableUtils.drawCenteredText(context, title,getX() + (getWidth() / 2), getY() + 10, true);

        int i = 20;
        for (String line : StringUtils.wrapLines(description, lineWrapWidth, true)) {
            DrawableUtils.drawCenteredText(context, "§7" + line,getX() + (getWidth() / 2), getY() + (i += 10), true);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getLineWrapWidth() {
        return lineWrapWidth;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean canDrag() {
        return true;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLineWrapWidth(int lineWrapWidth) {
        this.lineWrapWidth = lineWrapWidth;
    }
}
