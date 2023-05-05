package io.github.itzispyder.clickcrystals.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
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

    public static final int
            BANNER_TITLE_HEIGHT = 20,
            CATEGORY_MARGIN_LEFT = 10,
            CATEGORY_MARGIN_TOP = 10;

    public static final Map<Category,Integer> categoryMap = new ManualMap<Category,Integer>(
            Categories.CRYSTALLING,0,
            Categories.ANCHORING,1,
            Categories.RENDERING,2,
            Categories.OPTIMIZATION,3,
            Categories.MISC,4,
            Categories.OTHER,5
    ).getMap();

    private Set<CategoryWidget> categoryWidgets = new HashSet<>();

    public ClickCrystalsModuleScreen() {
        super(Text.literal("ClickCrystals Modules"));
    }

    @Override
    public void init() {
        final EmptyWidget bannerTitleWidget = new EmptyWidget(0, 0, this.width, BANNER_TITLE_HEIGHT, Text.literal("ClickCrystals - by ImproperIssues, TheTrouper"), 0xFF24A2A2);
        this.addDrawable(bannerTitleWidget);

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

            categoryWidget.setPosition(CATEGORY_MARGIN_LEFT + ((categoryWidget.getWidth() + 3) * i),CATEGORY_MARGIN_TOP + BANNER_TITLE_HEIGHT);
            moduleList.forEach(categoryWidget::addModule);
            this.categoryWidgets.add(categoryWidget);
            this.addDrawable(categoryWidget);
            categoryWidget.getModuleWidgets().forEach(this::addDrawableChild);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fillGradient(matrices, 0, 0, this.width, this.height, 0xD0000000, 0xD03873A9, 0);
        super.render(matrices, mouseX, mouseY, delta);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TexturesIdentifiers.SCREEN_BANNER_TEXTURE);
        DrawableHelper.drawTexture(matrices, CATEGORY_MARGIN_LEFT, 2, 0, 0, 64, 16, 64, 16);
    }
}
