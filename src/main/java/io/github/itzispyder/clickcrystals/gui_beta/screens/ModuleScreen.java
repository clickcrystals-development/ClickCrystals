package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui_beta.elements.client.CategoryElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.ModuleElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ModuleScreen extends DefaultBase {

    public static Category currentCategory = Categories.CRYSTALLING;
    private final List<ModuleElement> moduleElementList = new ArrayList<>();

    public ModuleScreen() {
        super("Module Screen");

        int caret = 40;
        for (Category category : Categories.getCategories().values()) {
            CategoryElement e = new CategoryElement(category, 10, caret);
            this.addChild(e);
            navlistModules.add(e);
            caret += 10;
        }

        this.filterByCategory(currentCategory);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawTexture(context, currentCategory.texture(), contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, currentCategory.name(), contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.DARK_GRAY.argb);
        caret -= 5;

        for (ModuleElement me : moduleElementList) {
            caret += 10;
            me.x = contentX;
            me.y = caret;
        }
    }

    public void filterByCategory(Category category) {
        children.removeIf(moduleElementList::contains);
        moduleElementList.clear();

        List<Module> list = system.modules().values().stream().filter(m -> m.getCategory() == category).toList();
        for (Module module : list) {
            ModuleElement e = new ModuleElement(module, 0, 0);
            this.addChild(e);
            moduleElementList.add(e);
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new ModuleScreen());
    }
}
