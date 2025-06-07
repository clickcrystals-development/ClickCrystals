package io.github.itzispyder.clickcrystals.gui.screens.modulescreen;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.CategoryElement;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.ModuleEditElement;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.SearchCategoryElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class OverviewScreen extends GuiScreen {

    private final List<CategoryElement> categories;
    private ModuleEditElement currentEditing;
    private final String switchModeText = "§e!§7 You're currently browsing ClickCrystals in §fOverview§7 mode,";
    private final AbstractElement switchModeButton = AbstractElement.create()
            .pos((int)(mc.textRenderer.getWidth(switchModeText) * 0.9 + 12), 2)
            .dimensions(180, 16)
            .onRender((context, mouseX, mouseY, button) -> {
                if (button.isHovered(mouseX, mouseY)) {
                    RenderUtils.fillRoundRect(context, button.x, button.y, button.width, button.height, 3, Shades.LIGHT_GRAY);
                }
                String text = "click to switch back to Browsing mode.";
                RenderUtils.drawText(context, text, button.x + 5, button.y + button.height / 3, 0.9F, false);
            })
            .onPress(button -> {
                ClickCrystals.config.setOverviewMode(false);
                ClickCrystals.config.save();
                mc.setScreen(new BrowsingScreen());
            })
            .build();

    public OverviewScreen() {
        super("Overview Module Screen");
        this.categories = new ArrayList<>();
        this.addChild(switchModeButton);

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

        this.selected = search.getSearchbar();
    }

    @Override
    protected void init() {
        for (CategoryElement ce : categories) {
            if (!ce.isCollapsed()) {
                this.bringForward(ce);
            }
        }
        ClickCrystals.config.loadOverviewScreen(this);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderOpaqueBackground(context);

        RenderUtils.fillRect(context, 0, 0, RenderUtils.width(), 20, 0x90000000);
        RenderUtils.drawText(context, switchModeText, 10, 20 / 3 + 1, 0.9F, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentEditing != null && !currentEditing.isMouseOver((int)mouseX, (int)mouseY)) {
            this.removeCurrentEditing();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        ClickCrystals.config.saveOverviewScreen(this);
        ClickCrystals.config.save();
        return true;
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

    public void bringForward(GuiElement child) {
        this.removeChild(child);
        this.addChild(child);
    }

    public List<CategoryElement> getCategories() {
        return categories;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new OverviewScreen());
    }
}
