package io.github.itzispyder.clickcrystals.guibeta;

import io.github.itzispyder.clickcrystals.guibeta.callbacks.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiScreen extends Screen {

    public final List<MouseMoveCallback> mouseMoveListeners;
    public final List<MouseClickCallback> mouseClickListeners;
    public final List<MouseClickCallback> mouseReleaseListeners;
    public final List<MouseDragCallback> mouseDragListeners;
    public final List<MouseScrollCallback> mouseScrollListeners;
    public final List<ScreenRenderCallback> screenRenderListeners;
    public final List<GuiElement> children;
    public GuiElement selected;

    public GuiScreen(String title) {
        super(Text.literal(title));

        this.mouseMoveListeners = new ArrayList<>();
        this.mouseClickListeners = new ArrayList<>();
        this.mouseReleaseListeners = new ArrayList<>();
        this.mouseDragListeners = new ArrayList<>();
        this.mouseScrollListeners = new ArrayList<>();
        this.screenRenderListeners = new ArrayList<>();
        this.children = new ArrayList<>();
        this.selected = null;
    }

    public abstract void baseRender(DrawContext context, int mouseX, int mouseY, float delta);

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.baseRender(context, mouseX, mouseY, delta);

        this.children.forEach(guiElement -> {
            guiElement.render(context, mouseX, mouseY);
        });

        this.screenRenderListeners.forEach(screenRenderCallback -> {
            screenRenderCallback.handleScreen(context, mouseX, mouseY, delta);
        });
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        this.mouseMoveListeners.forEach(mouseMoveCallback -> {
            mouseMoveCallback.handleMouse(mouseX, mouseY);
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        for (GuiElement child : children) {
            if (child.isHovered((int)mouseX, (int)mouseY)) {
                this.selected = child;
                child.onClick(mouseX, mouseY);
                break;
            }
        }

        this.mouseClickListeners.forEach(mouseClickCallback -> {
            mouseClickCallback.handleMouse(mouseX, mouseY, button, ClickType.CLICK);
        });

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);

        this.selected = null;
        this.mouseClickListeners.forEach(mouseClickCallback -> {
            mouseClickCallback.handleMouse(mouseX, mouseY, button, ClickType.RELEASE);
        });

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);

        if (selected != null && selected.draggable) {
            selected.move(deltaX, deltaY);
        }

        this.mouseDragListeners.forEach(mouseDragCallback -> {
            mouseDragCallback.handleMouse(mouseX, mouseY, button, deltaX, deltaY);
        });

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        super.mouseScrolled(mouseX, mouseY, amount);

        this.mouseScrollListeners.forEach(mouseScrollCallback -> {
            mouseScrollCallback.handleMouse(mouseX, mouseY, amount);
        });

        return true;
    }

    public List<GuiElement> getChildren() {
        return children;
    }

    public void clearChildren() {
        children.clear();
    }

    public void forEachChild(Consumer<GuiElement> action) {
        children.forEach(action);
    }

    public void addChild(GuiElement child) {
        if (child != null) {
            children.add(child);
        }
    }

    public void removeChild(GuiElement child) {
        children.remove(child);
    }
}
