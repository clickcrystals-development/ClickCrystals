package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryWidget extends CCWidget {

    public static final int
            TITLE_HEIGHT = 15,
            MARGIN_TOP = 0,
            MARGIN_BOTTOM = 1,
            MARGIN_LEFT = 1;

    private final Category category;
    private final Set<Module> modules;
    private final List<ModuleWidget> moduleWidgets;

    public CategoryWidget(int x, int y, int width, int height, Category category) {
        super(x, y, width, height, Text.literal(category.name()));
        this.modules = new HashSet<>();
        this.moduleWidgets = new ArrayList<>();
        this.category = category;
    }

    public CategoryWidget(Category category) {
        this(0, 0, ModuleWidget.DEFAULT_WIDTH + (2 * MARGIN_LEFT), 100, category);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fillGradient(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x500044AC, 0x50000000, 0);
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
        moduleWidget.setX(this.getX() + MARGIN_LEFT);
        moduleWidget.setY(this.getY() + moduleListHeight + (3 * MARGIN_BOTTOM));
    }

    public void recalculateDimensions() {
        final int moduleListHeight = ModuleWidget.DEFAULT_HEIGHT * moduleWidgets.size();
        final int moduleListWidth = ModuleWidget.DEFAULT_WIDTH;

        this.height = moduleListHeight + TITLE_HEIGHT + (MARGIN_BOTTOM + MARGIN_TOP);
        this.width = moduleListWidth + (int)(2 * MARGIN_LEFT);

        int i = 0;
        for (ModuleWidget moduleWidget : this.moduleWidgets) {
            final int yLvl = (++i) * ModuleWidget.DEFAULT_HEIGHT;

            moduleWidget.setX(this.getX() + MARGIN_LEFT);
            moduleWidget.setY(this.getY() + yLvl + (3 * MARGIN_BOTTOM));
        }
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public List<ModuleWidget> getDraggableChildren() {
        return moduleWidgets;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.width) && mouseY < (double)(this.getY() + TITLE_HEIGHT);
    }

    @Override
    public boolean canDrag() {
        return true;
    }
}