package io.github.itzispyder.clickcrystals.gui.elements.browsingmode;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class CategoryElement extends GuiElement {

    private final Category category;

    public CategoryElement(Category category, int x, int y) {
        super(x, y, 90, 10);
        super.setTooltip("Filter by category: " + category.name());
        this.category = category;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (BrowsingScreen.currentCategory == category && mc.currentScreen instanceof BrowsingScreen) {
            RoundRectBrush.drawRoundHoriLine(context, x, y, width, height, Gray.GENERIC_LOW);
        }
        else if (isHovered(mouseX, mouseY)) {
            RoundRectBrush.drawRoundHoriLine(context, x, y, width, height, Gray.LIGHT_GRAY);
        }
        RenderUtils.drawTexture(context, category.texture(), 10 + x + 1, y + 1, 8, 8);
        RenderUtils.drawText(context, category.name(), 15 + x + height - 2, y + height / 3, 0.65F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        BrowsingScreen.currentCategory = category;
        mc.setScreen(new BrowsingScreen());
    }

    public Category getCategory() {
        return category;
    }
}
