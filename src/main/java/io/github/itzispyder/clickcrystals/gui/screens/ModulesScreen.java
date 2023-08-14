package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.elements.cc.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.TabListElement;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModulesScreen extends ClickCrystalsBase {

    private static int prevCategoryTab = 0;
    private final List<ModuleElement> displayingModules = new ArrayList<>();

    public ModulesScreen() {
        super("ClickCrystals Modules Screen");
    }

    @Override
    protected void init() {
        // category bar
        TabListElement<Category> catBar = new TabListElement<>(Categories.getCategories().values().stream().toList(),nav.x + nav.width + 10, base.y + 10, (base.x + base.width) - (nav.x + nav.width + 20), 25, tabs -> {
            prevCategoryTab = tabs.getSelection();
            this.setCategory(tabs.getOptions().get(prevCategoryTab), nav.x + nav.width + 10, tabs.y + tabs.height + 10);
        }, Category::name);
        catBar.setSelection(prevCategoryTab);
        this.setCategory(catBar.getOptions().get(catBar.getSelection()), nav.x + nav.width + 10, catBar.y + catBar.height + 10);
        this.addChild(catBar);

        // callbacks
        this.screenRenderListeners.add((context, mouseX, mouseY, delta) -> displayingModules.forEach(moduleElement -> {
            moduleElement.render(context, mouseX, mouseY);
        }));

        // finish
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        for (ModuleElement element : displayingModules) {
            if (element.isHovered((int)mouseX, (int)mouseY)) {
                element.onClick(mouseX, mouseY, button);
            }
        }

        return true;
    }

    public void setCategory(Category category, int originX, int originY) {
        displayingModules.clear();

        List<Module> modules = system.modules().values().stream()
                .filter(m -> m.getCategory() == category)
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        int row, column;
        row = column = 0;

        for (Module module : modules) {
            ModuleElement me = new ModuleElement(module, 0, 0, 60);
            me.setX(originX + (me.width + 5) * column);
            me.setY(originY + (me.height + 3) * row);

            displayingModules.add(me);

            if (++column >= 4) {
                column = 0;
                row++;
            }
        }
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        ClickCrystalsBase.openClickCrystalsMenu();
    }
}
