package io.github.itzispyder.clickcrystals.gui_beta.elements.client;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.gui_beta.screens.ModuleScreen;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class CategoryElement extends GuiElement {

    private final Category category;

    public CategoryElement(Category category, int x, int y) {
        super(x, y, 90, 10);
        this.category = category;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            RoundRectBrush.drawRoundHoriLine(context, x, y, width, height, Gray.LIGHT_GRAY);
        }
        RenderUtils.drawTexture(context, category.texture(), 10 + x + 1, y + 1, 8, 8);
        RenderUtils.drawText(context, category.name(), 15 + x + height - 2, y + height / 3, 0.65F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        ModuleScreen.currentCategory = category;
    }

    public Category getCategory() {
        return category;
    }
}
