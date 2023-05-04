package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class CategoryWidget extends CCWidget {

    public static final int
            MARGIN_TOP = 10,
            MARGIN_LEFT = 3;

    private final Set<Module> modules;
    private final Set<ModuleWidget> moduleWidgets;

    public CategoryWidget(int x, int y, int width, int height, Category category) {
        super(x, y, width, height, Text.literal(category.getName()));
        this.modules = new HashSet<>();
        this.moduleWidgets = new HashSet<>();
    }

    public CategoryWidget(Category category) {
        this(0, 0, ModuleWidget.DEFAULT_WIDTH + (2 * MARGIN_LEFT), 100, category);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x40FFFFFF);
        DrawableHelper.drawBorder(matrices, getX(), getY(), getWidth(), getHeight(), 0xFFFFFFFF);
        DrawableHelper.drawCenteredTextWithShadow(matrices, mc.textRenderer, getMessage(), getX() + (getWidth() / 2), getY() + 5, 0xFFFFFFFF);
        DrawableHelper.fill(matrices, getX(), getY() + 15, getX() + getWidth() - 1, getY() + 16, 0xFFFFFFFF);
    }

    public void addModule(Module module) {
        final ModuleWidget moduleWidget = new ModuleWidget(module);

        modules.add(module);
        moduleWidgets.add(moduleWidget);

        final int moduleListHeight = ModuleWidget.DEFAULT_HEIGHT * moduleWidgets.size();
        final int moduleListWidth = ModuleWidget.DEFAULT_WIDTH;

        this.height = moduleListHeight + (int)(3 * MARGIN_TOP);
        this.width = moduleListWidth + (int)(2 * MARGIN_LEFT);
        moduleWidget.setX(getX() + MARGIN_LEFT);
        moduleWidget.setY(getY() + MARGIN_TOP + moduleListHeight);
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Set<ModuleWidget> getModuleWidgets() {
        return moduleWidgets;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        getModuleWidgets().forEach(moduleWidget -> {
            moduleWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        });
        return true;
    }
}
