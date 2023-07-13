package io.github.itzispyder.clickcrystals.gui.elements.design;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ScrollPanelElement extends GuiElement {

    private int minY, maxY;

    public ScrollPanelElement(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.minY = y;
        this.maxY = y + height;
    }

    public static boolean canRenderOnPanel(ScrollPanelElement panel, GuiElement element) {
        return element.y >= panel.y && element.y + element.height <= panel.y + panel.height;
    }

    public boolean canScrollUp() {
        return minY < y;
    }

    public boolean canScrollDown() {
        return maxY > y + height;
    }

    public boolean canScroll() {
        return canScrollUp() || canScrollDown();
    }

    public boolean canScrollInDirection(double amount) {
        if (amount >= 0.0) {
            return canScrollUp();
        }
        else {
            return canScrollDown();
        }
    }

    @Override
    public void addChild(GuiElement child) {
        super.addChild(child);
        updateBounds(child);
        child.scrollOnPanel(this, 0);
    }

    public void updateBounds(GuiElement child) {
        if (child.y < minY) {
            minY = child.y;
        }
        if (child.y + child.height > maxY) {
            maxY = child.y + child.height;
        }
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            for (int i = getChildren().size() - 1; i >= 0; i--) {
                GuiElement child = getChildren().get(i);
                if (child.isHovered((int)mouseX, (int)mouseY)) {
                    screen.selected = child;
                    child.onClick(mouseX, mouseY, button);
                    break;
                }
            }
        }
    }

    public void onScroll(double mouseX, double mouseY, double amount) {
        if (!canScrollInDirection(amount)) return;

        for (GuiElement child : getChildren()) {
            child.scrollOnPanel(this, (int)(amount * 6.9420));
        }
    }
}
