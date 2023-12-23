package io.github.itzispyder.clickcrystals.gui.screens.modulescreen;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.CategoryElement;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.ModuleEditElement;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.SearchCategoryElement;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class OverviewScreen extends GuiScreen {

    private final List<CategoryElement> categories;
    private ModuleEditElement currentEditing;

    public OverviewScreen() {
        super("Overview Module Screen");
        this.categories = new ArrayList<>();

        GridOrganizer grid = new GridOrganizer(105, 30, 90, 25, 3, 5);
        for (Category c : Categories.getCategories().values()) {
            CategoryElement ce = new CategoryElement(c, 0, 0, grid.getCellWidth());
            grid.addEntry(ce);
            categories.add(ce);
            this.addChild(ce);
        }
        grid.organize();

        SearchCategoryElement search = new SearchCategoryElement(10, 30, 90);
        this.addChild(search);
    }

    @Override
    protected void init() {
        for (CategoryElement ce : categories) {
            if (!ce.isCollapsed()) {
                this.removeChild(ce);
                this.addChild(ce);
            }
        }
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentEditing != null && !currentEditing.isMouseOver((int)mouseX, (int)mouseY)) {
            this.removeCurrentEditing();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void setCurrentEditing(Module module, int mouseX, int mouseY) {
        if (module == null) {
            return;
        }

        this.removeCurrentEditing();
        this.currentEditing = new ModuleEditElement(this, module, mouseX, mouseY);
        this.addChild(currentEditing);
        this.selected = currentEditing;
    }

    public void removeCurrentEditing() {
        this.selected = null;

        if (currentEditing == null) {
            return;
        }

        this.removeChild(currentEditing);
        ClickCrystals.config.saveModule(currentEditing.getModule());
        ClickCrystals.config.save();
        this.currentEditing = null;
    }

    public List<CategoryElement> getCategories() {
        return categories;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new OverviewScreen());
    }
}
