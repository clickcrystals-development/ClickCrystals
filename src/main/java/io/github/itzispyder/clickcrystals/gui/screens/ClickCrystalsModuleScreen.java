package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.widgets.CategoryWidget;
import io.github.itzispyder.clickcrystals.gui.widgets.EmptyWidget;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ManualMap;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class ClickCrystalsModuleScreen extends Screen {

    public static final Map<Category,Integer> categoryMap = new ManualMap<Category,Integer>(
            Categories.CRYSTALLING,0,
            Categories.ANCHORING,1,
            Categories.MISC,2,
            Categories.OPTIMIZATION,3,
            Categories.RENDERING,4,
            Categories.OTHER,5
    ).getMap();

    private Set<CategoryWidget> categoryWidgets = new HashSet<>();

    public ClickCrystalsModuleScreen() {
        super(Text.literal("ClickCrystals Modules"));
    }

    @Override
    public void init() {
        final EmptyWidget widget = new EmptyWidget(0, 0, this.width, 20, Text.literal("ClickCrystals - by ImproperIssues, TheTrouper"), 0xFF33CCFF);
        this.addDrawableChild(widget);

        for (Map.Entry<Category, Integer> entry : categoryMap.entrySet()) {
            final Category category = entry.getKey();
            final int i = entry.getValue();
            final CategoryWidget categoryWidget = new CategoryWidget(category);
            final List<Module> moduleList = system.modules()
                    .values()
                    .stream()
                    .filter(module -> module.getCategory() == category)
                    .sorted(Comparator.comparing(Module::getId))
                    .toList();

            categoryWidget.setPosition(20 + ((categoryWidget.getWidth() + 3) * i),30);
            moduleList.forEach(categoryWidget::addModule);
            this.categoryWidgets.add(categoryWidget);
            this.addDrawableChild(categoryWidget);
            categoryWidget.getModuleWidgets().forEach(this::addDrawableChild);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fillGradient(matrices, 0, 0, this.width, this.height, 0x9E000000, 0x9E3873A9);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
