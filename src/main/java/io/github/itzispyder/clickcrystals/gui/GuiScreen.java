package io.github.itzispyder.clickcrystals.gui;

import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.gui.callbacks.*;
import io.github.itzispyder.clickcrystals.gui.elements.Typeable;
import io.github.itzispyder.clickcrystals.gui.elements.design.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.GuiBorders;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiScreen extends Screen {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    protected static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public final List<MouseMoveCallback> mouseMoveListeners;
    public final List<MouseClickCallback> mouseClickListeners;
    public final List<MouseClickCallback> mouseReleaseListeners;
    public final List<MouseDragCallback> mouseDragListeners;
    public final List<MouseScrollCallback> mouseScrollListeners;
    public final List<ScreenRenderCallback> screenRenderListeners;
    public final List<KeyPressCallback> keyActionListeners;
    public final List<GuiElement> children;
    public GuiElement selected, mostRecentlyAdded;
    public boolean shiftKeyPressed, altKeyPressed, ctrlKeyPressed;

    public GuiScreen(String title) {
        super(Text.literal(title));

        this.mouseMoveListeners = new ArrayList<>();
        this.mouseClickListeners = new ArrayList<>();
        this.mouseReleaseListeners = new ArrayList<>();
        this.mouseDragListeners = new ArrayList<>();
        this.mouseScrollListeners = new ArrayList<>();
        this.screenRenderListeners = new ArrayList<>();
        this.keyActionListeners = new ArrayList<>();
        this.children = new ArrayList<>();
        this.selected = null;
        this.mostRecentlyAdded = null;
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

        Module guiBorders = Module.get(GuiBorders.class);
        if (guiBorders.isEnabled()) {
            GuiElement element = getHoveredElement(mouseX, mouseY);
            if (element != null) {
                tagGuiElement(context, mouseX, mouseY, element);
            }
        }
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
        for (int i = children.size() - 1; i >= 0; i--) {
            GuiElement child = children.get(i);
            if (child.isHovered((int)mouseX, (int)mouseY)) {
                this.selected = child;
                child.mouseClicked(mouseX, mouseY, button);
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

        if (!(selected instanceof Typeable)) {
            this.selected = null;
        }

        this.mouseReleaseListeners.forEach(mouseClickCallback -> {
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

        for (GuiElement child : children) {
            if (child instanceof ScrollPanelElement panel && panel.isMouseOver((int)mouseX, (int)mouseY)) {
                panel.onScroll(amount);
                break;
            }
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.shiftKeyPressed = true;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_ALT || keyCode == GLFW.GLFW_KEY_RIGHT_ALT) {
            this.altKeyPressed = true;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            this.ctrlKeyPressed = true;
        }

        super.keyPressed(keyCode, scanCode, modifiers);

        this.keyActionListeners.forEach(keyPressCallback -> {
            keyPressCallback.handleKey(keyCode, ClickType.CLICK, scanCode, modifiers);
        });

        if (selected instanceof Typeable typeable) {
            typeable.onKey(keyCode, scanCode);
        }

        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.shiftKeyPressed = false;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_ALT || keyCode == GLFW.GLFW_KEY_RIGHT_ALT) {
            this.altKeyPressed = false;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            this.ctrlKeyPressed = false;
        }

        super.keyReleased(keyCode, scanCode, modifiers);

        this.keyActionListeners.forEach(keyPressCallback -> {
            keyPressCallback.handleKey(keyCode, ClickType.RELEASE, scanCode, modifiers);
        });

        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !(selected instanceof Typeable);
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
            mostRecentlyAdded = child;
            children.add(child);
        }
    }

    public void removeChild(GuiElement child) {
        children.remove(child);
    }

    public void tagGuiElement(DrawContext context, int mouseX, int mouseY, GuiElement element) {
        String name = element.getClass().getSimpleName();
        double textScale = 0.7;
        int width = mc.textRenderer.getWidth(name) + 2;
        DrawableUtils.fill(context, mouseX, mouseY, (int)(width * textScale), 9, 0xFF000000);
        DrawableUtils.drawText(context, name, mouseX + 2, mouseY + (int)(9 * 0.33), 0.7F, true);
    }

    public GuiElement getHoveredElement(double mouseX, double mouseY) {
        for (int i = children.size() - 1; i >= 0; i--) {
            GuiElement child = children.get(i);
            if (child.isHovered((int)mouseX, (int)mouseY)) {
                return child;
            }
        }
        return null;
    }
}
