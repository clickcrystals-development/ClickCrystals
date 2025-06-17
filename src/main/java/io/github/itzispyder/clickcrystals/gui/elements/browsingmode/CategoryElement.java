package io.github.itzispyder.clickcrystals.gui.elements.browsingmode;

import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
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
            RenderUtils.fillRoundHoriLine(context, x, y, width, height, Shades.GENERIC_LOW);
            RenderUtils.fillRoundShadow(context, x, y, width, height, height / 2, 3, 0x8000B7FF, 0x0000B7FF);
        }
        else if (isHovered(mouseX, mouseY)) {
            RenderUtils.fillRoundHoriLine(context, x, y, width, height, Shades.LIGHT_GRAY);
        }
        RenderUtils.drawTexture(context, category.texture(), 10 + x + 1, y + 1, 8, 8);
        RenderUtils.drawText(context, category.name(), 15 + x + height - 2, y + height / 3, 0.65F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        BrowsingScreen.currentCategory = category;
        UserInputListener.openModulesScreen();
    }

    public Category getCategory() {
        return category;
    }
}
