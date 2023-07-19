package io.github.itzispyder.clickcrystals.gui.elements.design;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ScrollPanelElement extends GuiElement {

    public static final int SCROLL_MULTIPLIER = 15;
    private int remainingUp, remainingDown;

    public ScrollPanelElement(int x, int y, int width, int height) {
        super(x, y, width, height);
        remainingUp = remainingDown = 0;
    }

    public static boolean canRenderOnPanel(ScrollPanelElement panel, GuiElement element) {
        return element.y >= panel.y && element.y + element.height <= panel.y + panel.height;
    }

    public boolean canScrollDown() {
        return remainingDown > 0;
    }

    public boolean canScrollUp() {
        return remainingUp > 0;
    }

    public boolean canScroll() {
        return canScrollUp() || canScrollDown();
    }

    public boolean canScrollInDirection(int amount) {
        if (amount >= 0) {
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
        if (child.y < y) {
            remainingUp += (y - child.y);
        }
        if (child.y + child.height > y + height) {
            remainingDown += ((child.y + child.height) - (y + height));
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
        for (int i = 0; i < SCROLL_MULTIPLIER; i++) {
            if (canScrollInDirection((int)amount)) {
                for (GuiElement child : getChildren()) {
                    child.scrollOnPanel(this, (int)amount);
                }

                remainingDown += amount;
                remainingUp -= amount;
            }
        }
    }
}
