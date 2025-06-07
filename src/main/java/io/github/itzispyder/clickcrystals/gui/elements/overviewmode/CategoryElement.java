package io.github.itzispyder.clickcrystals.gui.elements.overviewmode;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class CategoryElement extends GuiElement {

    private final Category category;
    private final GridOrganizer modules;
    private boolean collapsed;
    private int lastClickX, lastClickY;

    public CategoryElement(Category category, int x, int y, int width) {
        super(x, y, width, 0);
        this.setDraggable(true);
        this.category = category;
        this.modules = new GridOrganizer(x + 5, y + 20, width - 10, 10, 1, 2);

        for (Module m : system.getModuleByCategory(category)) {
            ModuleElement me = new ModuleElement(m, 0, 0, modules.getCellWidth(), modules.getCellHeight());
            modules.addEntry(me);
            this.addChild(me);
        }
        modules.organize();
        this.setCollapsed(true);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.fillRoundRect(context, x, y, width, height, 5, Shades.TRANS_BLACK);
        RenderUtils.drawTexture(context, category.texture(), x + 5, y + 7, 10, 10);
        RenderUtils.drawText(context, category.name(), x + 18, y + 9, 0.9F, false);
        RenderUtils.drawText(context, collapsed ? "§7>" : "§b^", x + width - 10, y + 9, 1.0F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.lastClickX = x;
        this.lastClickY = y;
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int button) {
        if (button == 0 && isHoverCollapsion((int)mouseX, (int)mouseY) && lastClickX == x && lastClickY == y) {
            setCollapsed(!isCollapsed());
            if (mc.currentScreen instanceof OverviewScreen screen) {
                screen.bringForward(this);
            }
        }
    }

    public Category getCategory() {
        return category;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
        this.height = collapsed ? getCollapsedHeight() : getUncollapsedHeight();

        modules.getEntries().forEach(m -> m.setRendering(!collapsed));
    }

    public int getCollapsedHeight() {
        return 25;
    }

    public int getUncollapsedHeight() {
        return getCollapsedHeight() + modules.getEntries().size() * (modules.getCellHeight() + modules.getGap());
    }

    public boolean isHoverCollapsion(int mouseX, int mouseY) {
        return rendering && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + getCollapsedHeight() - 5;
    }
}
