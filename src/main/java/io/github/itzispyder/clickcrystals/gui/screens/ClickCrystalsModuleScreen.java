package io.github.itzispyder.clickcrystals.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.gui.display.TextLabelElement;
import io.github.itzispyder.clickcrystals.gui.display.WindowContainerElement;
import io.github.itzispyder.clickcrystals.gui.widgets.CategoryWidget;
import io.github.itzispyder.clickcrystals.gui.widgets.EmptyWidget;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ManualMap;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
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

    private final Set<CategoryWidget> categoryWidgets;
    private WindowContainerElement descriptionWindow;
    public Module selectedModule;
    public boolean isEditingModule;

    public ClickCrystalsModuleScreen() {
        super(Text.literal("ClickCrystals Modules"));
        this.categoryWidgets = new HashSet<>();
        this.isEditingModule = false;
        this.descriptionWindow = new WindowContainerElement(0, 0, 0, 0, "", "", 30);

        final TextLabelElement label = new TextLabelElement(0, 0, "X");
        label.setPressAction(button -> {
            ClickCrystals.CC_MODULE_SCREEN.isEditingModule = false;
            ClickCrystals.CC_MODULE_SCREEN.selectedModule = null;
        });
        this.descriptionWindow.addChild(label);
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

            categoryWidget.setX(CATEGORY_MARGIN_LEFT + ((categoryWidget.getWidth() + 3) * i));
            categoryWidget.setY(CATEGORY_MARGIN_TOP + BANNER_TITLE_HEIGHT);
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

        if (this.selectedModule != null) {
            this.renderDescription(matrices, mouseX, mouseY, this.selectedModule);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !this.isEditingModule;
    }

    @Override
    public void close() {
        this.isEditingModule = false;
        this.selectedModule = null;

        mc.setScreen((Screen)null);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.handleDescriptionClose(mouseX, mouseY, button);

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (!this.isEditingModule) return false;
            this.close();
        }

        return true;
    }

    private void renderDescription(MatrixStack matrices, double mouseX, double mouseY, Module module) {
        final Window win = mc.getWindow();
        final int winWidth = win.getScaledWidth();
        final int winHeight = win.getScaledHeight();
        final int left = winWidth / 4;
        final int top = winHeight / 4;
        final int width = winWidth / 2;
        final int height = winHeight / 2;

        final TextLabelElement label = (TextLabelElement)descriptionWindow.getChildren().get(0);

        descriptionWindow.setX(left);
        descriptionWindow.setY(top);
        descriptionWindow.setWidth(width);
        descriptionWindow.setHeight(height);
        descriptionWindow.setTitle(module.getName());
        descriptionWindow.setDescription(module.getDescription());

        label.setX(left + width - mc.textRenderer.getWidth("X"));
        label.setY(top + 10);
        label.setText((label.isMouseOver(mouseX, mouseY) ? "§b" : "§f") + "§lX");

        descriptionWindow.render(matrices, mouseX, mouseY);
        matrices.translate(0.0F, 0.0F, 69.0F);
    }

    public CategoryWidget getHoveredCategory(double mouseX, double mouseY) {
        for (CategoryWidget categoryWidget : categoryWidgets) {
            if (categoryWidget.isMouseOver(mouseX, mouseY)) {
                return categoryWidget;
            }
        }
        return null;
    }

    private void handleDescriptionClose(double mouseX, double mouseY, int button) {
        if (descriptionWindow == null) return;

        final TextLabelElement label = (TextLabelElement)descriptionWindow.getChildren().get(0);

        if (button == 0) {
            if (label == null) return;
            label.onClick(mouseX, mouseY, button);
        }
    }
}
