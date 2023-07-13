package io.github.itzispyder.clickcrystals.gui.elements.design;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ScrollPanelElement extends GuiElement {

    public ScrollPanelElement(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        if (rendering) {
            onRender(context, mouseX, mouseY);

            for (GuiElement child : getChildren()) {
                if (child.y >= y && child.y + child.height <= y + height) {
                    child.render(context, mouseX, mouseY);
                }
            }
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
        for (GuiElement child : getChildren()) {
            child.move(0, amount * 2);
        }
    }
}
