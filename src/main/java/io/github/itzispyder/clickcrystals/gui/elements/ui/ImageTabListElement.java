package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.List;
import java.util.function.Consumer;

public class ImageTabListElement extends TabListElement<ImageElement> {

    public ImageTabListElement(List<ImageElement> options, int x, int y, int width, int height, Consumer<TabListElement<ImageElement>> pressAction) {
        super(options, x, y, width, height, pressAction);
    }

    @Override
    protected void initTabList(ImageElement background) {
        double sectionWidth = (double)background.width / options.size();

        int i = 0;
        for (ImageElement tab : options) {
            tab.setHeight(background.height - 4);
            tab.setWidth(tab.height);
            tab.setX(x + (int)(sectionWidth * i++ + sectionWidth / 2 - tab.width / 2));
            tab.setY(background.y + 2);
            background.addChild(tab);
        }
    }

    @Override
    protected void renderExtras(DrawContext context, int mouseX, int mouseY) {
        double sectionWidth = (double)width / options.size();
        int selectionX = x + (int)sectionWidth * selection;

        DrawableUtils.drawHorizontalLine(context, selectionX + 2, y + height - 1, (int)sectionWidth, 2, 0xD03873A9);

        for (int i = 1; i < options.size(); i++) {
            DrawableUtils.drawText(context, "§7|", x + (int)(sectionWidth * i), y + (int)(height * 0.5), 0.6F, false);
        }
    }
}
