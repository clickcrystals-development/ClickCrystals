package io.github.itzispyder.clickcrystals.gui.screens.modulescreen;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.CategoryElement;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class OverviewScreen extends GuiScreen {

    private final List<CategoryElement> categories;

    public OverviewScreen() {
        super("Overview Module Screen");
        this.categories = new ArrayList<>();

        if (mc.player == null || mc.world == null) {
            mc.setScreen(new BrowsingScreen());
            return;
        }


        GridOrganizer grid = new GridOrganizer(10, 30, 90, 0, 99, 5);
        for (Category c : Categories.getCategories().values()) {
            CategoryElement ce = new CategoryElement(c, 0, 0, grid.getCellWidth());
            grid.addEntry(ce);
            categories.add(ce);
            this.addChild(ce);
        }
        grid.organize();
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    public List<CategoryElement> getCategories() {
        return categories;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new OverviewScreen());
    }
}
