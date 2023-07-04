package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.data.ConfigSection;
import io.github.itzispyder.clickcrystals.data.Delta3d;
import io.github.itzispyder.clickcrystals.guibeta.ClickType;
import io.github.itzispyder.clickcrystals.gui.DisplayableElement;
import io.github.itzispyder.clickcrystals.gui.Draggable;
import io.github.itzispyder.clickcrystals.guibeta.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.gui.display.TextLabelElement;
import io.github.itzispyder.clickcrystals.gui.display.WindowContainerElement;
import io.github.itzispyder.clickcrystals.gui.widgets.CategoryWidget;
import io.github.itzispyder.clickcrystals.gui.widgets.EmptyWidget;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

public class ClickCrystalsModuleScreen extends Screen {

    public static final int
            BANNER_TITLE_HEIGHT = 20,
            CATEGORY_MARGIN_LEFT = 10,
            CATEGORY_MARGIN_TOP = 10;

    private final Set<CategoryWidget> categoryWidgets;
    public TextLabelElement resetLabel, discordLabel;
    private Draggable selectedDraggable;
    public WindowContainerElement descriptionWindow;
    public Module selectedModule;
    public boolean isEditingModule;
    public Delta3d predragDelta;

    public ClickCrystalsModuleScreen() {
        super(Text.literal("ClickCrystals Modules"));

        this.categoryWidgets = new HashSet<>();
        this.selectedDraggable = null;
        this.isEditingModule = false;
        this.descriptionWindow = new WindowContainerElement(0, 0, 200, 90, "", "", 25);
        this.descriptionWindow.setFillColor(0x90000000);
        this.predragDelta = new Delta3d(0, 0, 0);

        final TextLabelElement exitLabel = new TextLabelElement(0, 0, "X");
        final TextLabelElement toggleLabel = new TextLabelElement(0, 0, "▶");

        exitLabel.setPressAction(button -> {
            ClickCrystalsModuleScreen.this.isEditingModule = false;
            ClickCrystalsModuleScreen.this.selectedModule = null;
        });
        toggleLabel.setPressAction(button -> {
            Module module = ClickCrystalsModuleScreen.this.selectedModule;
            if (module == null) return;
            module.setEnabled(!module.isEnabled(), false);
        });

        this.descriptionWindow.addChild(exitLabel);
        this.descriptionWindow.addChild(toggleLabel);
    }

    @Override
    public void init() {
        final EmptyWidget bannerTitleWidget = new EmptyWidget(0, 0, this.width, BANNER_TITLE_HEIGHT, Text.literal("§fClickCrystals - by §bImproperIssues§f, §bTheTrouper"), 0xFF24A2A2);
        final List<Category> categories = Categories.getCategories().values().stream()
                .sorted(Comparator.comparing(Category::name))
                .toList();

        this.addDrawable(bannerTitleWidget);
        this.initLabels();

        int i = 0;
        for (Category category : categories) {
            final CategoryWidget categoryWidget = new CategoryWidget(category);
            final List<Module> moduleList = system.modules().values().stream()
                    .filter(module -> module.getCategory() == category)
                    .sorted(Comparator.comparing(Module::getId))
                    .toList();

            categoryWidget.setX(CATEGORY_MARGIN_LEFT + ((categoryWidget.getWidth() + 3) * (i++)));
            categoryWidget.setY(CATEGORY_MARGIN_TOP + BANNER_TITLE_HEIGHT);
            moduleList.forEach(categoryWidget::addModule);
            this.categoryWidgets.add(categoryWidget);
            this.addDrawable(categoryWidget);
            categoryWidget.getDraggableChildren().forEach(this::addDrawableChild);
        }
        this.loadFromConfig();
        this.categoryWidgets.forEach(CategoryWidget::recalculateDimensions);
    }

    private void initLabels() {
        final Window win = mc.getWindow();
        final int winWidth = win.getScaledWidth();
        final int winHeight = win.getScaledHeight();
        int marginRight = winWidth - CATEGORY_MARGIN_LEFT;

        this.resetLabel = new TextLabelElement(0, 2, "Reset");
        this.resetLabel.updateWidth();
        this.resetLabel.setFillColor(0x90000000);
        this.resetLabel.setHeight(16);
        marginRight -= resetLabel.getWidth();
        this.resetLabel.setX(marginRight);
        this.resetLabel.setPressAction(button -> {
            ClickCrystalsModuleScreen.this.clearAndReset();
        });

        this.discordLabel = new TextLabelElement(0, 2, "Discord");
        this.discordLabel.updateWidth();
        this.discordLabel.setFillColor(0x90000000);
        this.discordLabel.setHeight(16);
        marginRight -= discordLabel.getWidth();
        this.discordLabel.setX(marginRight);
        this.discordLabel.setPressAction(button -> {
            system.openUrl("https://discord.gg/tMaShNzNtP", starter + "§bJoin Our Discord for the Latest Updates!", new ClickCrystalsModuleScreen());
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 0, 0xD0000000, 0xD03873A9);
        super.render(context, mouseX, mouseY, delta);

        context.drawTexture(TexturesIdentifiers.SCREEN_BANNER_TEXTURE, CATEGORY_MARGIN_LEFT, 2, 0, 0, 64, 16, 64, 16);

        this.handleDraggableDrag(mouseX, mouseY);
        this.resetLabel.render(context, mouseX, mouseY);
        this.discordLabel.render(context, mouseX, mouseY);
        this.renderDescription(context, mouseX, mouseY, this.selectedModule);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void close() {
        super.close();
        this.saveToConfig();
        this.isEditingModule = false;
        this.selectedModule = null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.handleDescriptionClose(mouseX, mouseY, button);
        this.handleDraggableDrag(mouseX, mouseY, button, ClickType.CLICK);

        if (resetLabel.isMouseOver(mouseX, mouseY)) resetLabel.onClick(mouseX, mouseY, button);
        if (discordLabel.isMouseOver(mouseX, mouseY)) discordLabel.onClick(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        this.handleDraggableDrag(mouseX, mouseY, button, ClickType.RELEASE);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
        }

        return true;
    }

    private void renderDescription(DrawContext context, double mouseX, double mouseY, Module module) {
        if (module == null) return;

        final Window win = mc.getWindow();
        final int winWidth = win.getScaledWidth();
        final int winHeight = win.getScaledHeight();
        final int left = winWidth / 4;
        final int top = winHeight / 4;

        descriptionWindow.setTitle(module.getName() + ": " + module.getToggledStateMessage());
        descriptionWindow.setDescription(module.getDescription());
        descriptionWindow.checkBounds();

        final TextLabelElement exitLabel = (TextLabelElement)descriptionWindow.getDraggableChildren().get(0);
        final TextLabelElement toggleLabel = (TextLabelElement)descriptionWindow.getDraggableChildren().get(1);

        exitLabel.setText((exitLabel.isMouseOver(mouseX, mouseY) ? "§b" : "§f") + "§lX");
        toggleLabel.setText((toggleLabel.isMouseOver(mouseX, mouseY) ? "§b" : "§f") + "§l▶");

        int i = 0;
        for (DisplayableElement child : descriptionWindow.getDraggableChildren()) {
            child.setFillColor(child.isMouseOver(mouseX, mouseY) ? 0xD0303030 : 0xD0000000);
            child.setWidth(15);
            child.setHeight(15);
            child.setX(descriptionWindow.getX() + descriptionWindow.getWidth() - 4);
            child.setY(descriptionWindow.getY() + 5 + ((child.getWidth() + 3) * i++));
        }

        descriptionWindow.render(context, mouseX, mouseY);
        context.getMatrices().translate(0.0F, 0.0F, 69.0F);
    }

    public Draggable getHoveredDraggable(double mouseX, double mouseY) {
        if (isEditingModule && descriptionWindow.isMouseOver(mouseX, mouseY, false)) {
            return descriptionWindow;
        }
        for (CategoryWidget categoryWidget : categoryWidgets) {
            if (categoryWidget.isMouseOver(mouseX, mouseY)) {
                return categoryWidget;
            }
        }
        return null;
    }

    private void handleDescriptionClose(double mouseX, double mouseY, int button) {
        if (descriptionWindow == null) return;

        if (button == 0) {
            for (DisplayableElement child : descriptionWindow.getDraggableChildren()) {
                if (child == null) return;
                child.onClick(mouseX, mouseY, button);
            }
        }
    }

    private void handleDraggableDrag(double mouseX, double mouseY, int button, ClickType click) {
        this.selectedDraggable = null;
        this.saveToConfig();

        if (button == 0) {
            switch (click) {
                case CLICK -> {
                    this.selectedDraggable = this.getHoveredDraggable(mouseX, mouseY);
                    this.predragDelta = new Delta3d(mouseX, mouseY, 0);
                }
                case RELEASE -> {
                    this.selectedDraggable = null;
                }
            }
        }
    }

    private void handleDraggableDrag(double mouseX, double mouseY) {
        if (this.selectedDraggable == null) return;

        final Delta3d postdrag = new Delta3d(mouseX, mouseY, 0);

        selectedDraggable.dragAlong(this.predragDelta, postdrag);
        this.predragDelta = postdrag;
        this.saveToConfig();
    }

    private void clearAndReset() {
        this.close();
        this.clearFromConfig();
        this.selectedDraggable = null;
        this.selectedModule = null;
        this.isEditingModule = false;
        this.categoryWidgets.clear();
        mc.setScreen(new ClickCrystalsModuleScreen());
    }

    private void clearFromConfig() {
        for (CategoryWidget categoryWidget : this.categoryWidgets) {
            final Category category = categoryWidget.getCategory();
            config.set("categories." + category.name() + ".x", null);
            config.set("categories." + category.name() + ".y", null);
        }
        config.save();
    }

    private void loadFromConfig() {
        for (CategoryWidget categoryWidget : this.categoryWidgets) {
            final Category category = categoryWidget.getCategory();
            if (config.get("categories." + category.name() + ".x") == null) continue;
            if (config.get("categories." + category.name() + ".y") == null) continue;
            categoryWidget.setX(config.get("categories." + category.name() + ".x", Integer.class));
            categoryWidget.setY(config.get("categories." + category.name() + ".y", Integer.class));
        }
    }

    private void saveToConfig() {
        for (CategoryWidget categoryWidget : this.categoryWidgets) {
            final Category category = categoryWidget.getCategory();
            config.set("categories." + category.name() + ".x", new ConfigSection<>(categoryWidget.getX()));
            config.set("categories." + category.name() + ".y", new ConfigSection<>(categoryWidget.getY()));
        }
        config.save();
    }
}