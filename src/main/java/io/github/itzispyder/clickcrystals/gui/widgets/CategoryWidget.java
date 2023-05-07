package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public class CategoryWidget extends CCWidget {

    public static final int
            TITLE_HEIGHT = 15,
            MARGIN_TOP = 0,
            MARGIN_BOTTOM = 1,
            MARGIN_LEFT = 1;

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
        DrawableHelper.fillGradient(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x100044AC, 0x10FFFFFF, 0);
        DrawableUtils.drawBorder(matrices, getX(), getY(), getWidth(), getHeight(), 0xFF1A9494);
        DrawableHelper.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + TITLE_HEIGHT, 0xFF1A9494);
        DrawableUtils.drawCenteredText(matrices, getMessage(), getX() + (getWidth() / 2), getY() + 5, true);
    }

    public void addModule(Module module) {
        final ModuleWidget moduleWidget = new ModuleWidget(module);

        modules.add(module);
        moduleWidgets.add(moduleWidget);

        final int moduleListHeight = ModuleWidget.DEFAULT_HEIGHT * moduleWidgets.size();
        final int moduleListWidth = ModuleWidget.DEFAULT_WIDTH;

        this.height = moduleListHeight + TITLE_HEIGHT + (MARGIN_BOTTOM + MARGIN_TOP);
        this.width = moduleListWidth + (int)(2 * MARGIN_LEFT);
        moduleWidget.setX(getX() + MARGIN_LEFT);
        moduleWidget.setY(getY() + moduleListHeight + (3 * MARGIN_BOTTOM));
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Set<ModuleWidget> getModuleWidgets() {
        return moduleWidgets;
    }

    @Override
    public void move(int delX, int delY) {
        super.move(delX, delY);
        this.moduleWidgets.forEach(moduleWidget -> {
            moduleWidget.move(delX, delY);
        });
    }

    @Override
    public void moveTo(int x, int y) {
        super.moveTo(x, y);
        this.moduleWidgets.forEach(moduleWidget -> {
            moduleWidget.moveTo(x, y);
        });
    }

    @Override
    public void move(double delX, double delY) {
        super.move(delX, delY);
        this.moduleWidgets.forEach(moduleWidget -> {
            moduleWidget.move(delX, delY);
        });
    }

    @Override
    public void moveTo(double x, double y) {
        super.moveTo(x, y);
        this.moduleWidgets.forEach(moduleWidget -> {
            moduleWidget.moveTo(x, y);
        });
    }
}
