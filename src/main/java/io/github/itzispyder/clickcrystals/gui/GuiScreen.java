package io.github.itzispyder.clickcrystals.gui;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.elements.common.Typeable;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.callbacks.*;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.GuiBorders;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiScreen extends Screen implements Global {

    public final List<MouseMoveCallback> mouseMoveListeners;
    public final List<MouseClickCallback> mouseClickListeners;
    public final List<MouseDragCallback> mouseDragListeners;
    public final List<MouseScrollCallback> mouseScrollListeners;
    public final List<ScreenRenderCallback> screenRenderListeners;
    public final List<KeyPressCallback> keyActionListeners;
    public final List<GuiElement> children;
    public GuiElement selected, hovered, mostRecentlyAdded;
    public long lastHover;
    public boolean shiftKeyPressed, altKeyPressed, ctrlKeyPressed;
    public Pair<Integer, Integer> cursor;

    public GuiScreen(String title) {
        super(Text.literal(title));

        this.lastHover = System.currentTimeMillis();
        this.mouseMoveListeners = new ArrayList<>();
        this.mouseClickListeners = new ArrayList<>();
        this.mouseDragListeners = new ArrayList<>();
        this.mouseScrollListeners = new ArrayList<>();
        this.screenRenderListeners = new ArrayList<>();
        this.keyActionListeners = new ArrayList<>();
        this.children = new ArrayList<>();
        this.selected = null;
        this.mostRecentlyAdded = null;
        this.cursor = Pair.of(0, 0);
    }

    public static boolean matchCurrent(Class<? extends GuiScreen> type) {
        return mc.currentScreen != null && mc.currentScreen.getClass() == type;
    }

    public abstract void baseRender(DrawContext context, int mouseX, int mouseY, float delta);

    @Override
    public void tick() {
        for (GuiElement child : children) {
            child.onTick();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (selected != null && selected.isDraggable()) {
            int dx = mouseX - cursor.left;
            int dy = mouseY - cursor.right;
            selected.move(dx, dy);
            selected.boundIn(context.getScaledWindowWidth(), context.getScaledWindowHeight());
            this.cursor = Pair.of(mouseX, mouseY);
        }

        super.render(context, mouseX, mouseY, delta);
        this.baseRender(context, mouseX, mouseY, delta);

        try {
            for (GuiElement guiElement : children) {
                guiElement.render(context, mouseX, mouseY);
            }
            for (ScreenRenderCallback callback : screenRenderListeners) {
                callback.handleScreen(context, mouseX, mouseY, delta);
            }
        }
        catch (ConcurrentModificationException ignore) {}

        Module guiBorders = Module.get(GuiBorders.class);
        GuiElement element = getHoveredElement(mouseX, mouseY);

        if (guiBorders.isEnabled()) {
            if (element != null) {
                tagGuiElement(context, mouseX, mouseY, element);
            }
        }
        if (hovered != element) {
            hovered = element;
            lastHover = System.currentTimeMillis();
        }
        else if (hovered != null && hovered.hasTooltip()) {
            if (System.currentTimeMillis() > lastHover + hovered.getTooltipDelay()) {
                hovered.renderTooltip(context, mouseX, mouseY);
            }
        }
    }

    public void renderOpaqueBackground(DrawContext context) {
        if (PlayerUtils.invalid()) {
            renderPanoramaBackground(context,mc.getRenderTickCounter().getTickProgress(true));
            context.drawTexture(RenderPipelines.GUI_TEXTURED, Tex.Defaults.OPTIONS_BACKGROUND, 0, 0, 0, 0.0F, 0, this.width, this.height, 32, 32);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        for (MouseMoveCallback callback : mouseMoveListeners) {
            callback.handleMouse(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        int mouseX = (int) click.x();
        int mouseY = (int) click.y();
        int button = click.button();

        for (int i = children.size() - 1; i >= 0; i--) {
            GuiElement child = children.get(i);
            if (child.isMouseOver(mouseX, mouseY)) {
                this.selected = child;
                this.cursor = Pair.of(mouseX, mouseY);
                child.mouseClicked(mouseX, mouseY, button);
                break;
            }
        }

        for (MouseClickCallback callback : mouseClickListeners) {
            callback.handleMouse(mouseX, mouseY, button, ClickType.CLICK);
        }

        return true;
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (selected instanceof Typeable typeable) {
            typeable.onChar((char)input.codepoint(), input.modifiers());
            return true;
        }
        return super.charTyped(input);
    }

    @Override
    public boolean mouseReleased(Click click) {
        super.mouseReleased(click);

        if (!(selected instanceof Typeable)) {
            this.selected = null;
        }

        int mouseX = (int) click.x();
        int mouseY = (int) click.y();
        int button = click.button();

        for (int i = children.size() - 1; i >= 0; i--) {
            GuiElement child = children.get(i);
            if (child.isMouseOver(mouseX, mouseY)) {
                child.mouseReleased(mouseX, mouseY, button);
                break;
            }
        }

        for (MouseClickCallback callback : mouseClickListeners) {
            callback.handleMouse(mouseX, mouseY, button, ClickType.RELEASE);
        }

        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        super.mouseDragged(click, offsetX, offsetY);

        for (MouseDragCallback callback : mouseDragListeners) {
            callback.handleMouse(click.x(), click.y(), click.button(), offsetX, offsetY);
        }

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

        for (MouseScrollCallback callback : mouseScrollListeners) {
            callback.handleMouse(mouseX, mouseY, verticalAmount);
        }

        for (GuiElement child : children) {
            if (child.isMouseOver((int)mouseX, (int)mouseY)) {
                child.mouseScrolled(mouseX, mouseY, (int)verticalAmount);
                if (child instanceof ScrollPanelElement panel) {
                    panel.onScroll(verticalAmount);
                }
            }
            scrollAt(child, (int)mouseX, (int)mouseY, verticalAmount);
        }

        return true;
    }

    private void scrollAt(GuiElement element, int mouseX, int mouseY, double amount) {
        if (element instanceof ScrollPanelElement panel && panel.isMouseOver(mouseX, mouseY)) {
            panel.onScroll(amount);
            return;
        }

        for (GuiElement child : element.getChildren()) {
            scrollAt(child, mouseX, mouseY, amount);
        }
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        int keyCode = input.getKeycode();
        int scanCode = input.scancode();
        int modifiers = input.modifiers();

        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.shiftKeyPressed = true;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_ALT || keyCode == GLFW.GLFW_KEY_RIGHT_ALT) {
            this.altKeyPressed = true;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            this.ctrlKeyPressed = true;
        }

        if (selected instanceof Typeable typeable && typeable.onKey(keyCode, scanCode)) {
            return true;
        }

        for (KeyPressCallback callback : keyActionListeners) {
            callback.handleKey(keyCode, ClickType.CLICK, scanCode, modifiers);
        }

        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        int keyCode = input.getKeycode();
        int scanCode = input.scancode();
        int modifiers = input.modifiers();

        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.shiftKeyPressed = false;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_ALT || keyCode == GLFW.GLFW_KEY_RIGHT_ALT) {
            this.altKeyPressed = false;
        }
        else if (keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            this.ctrlKeyPressed = false;
        }

        super.keyReleased(input);

        for (KeyPressCallback callback : keyActionListeners) {
            callback.handleKey(keyCode, ClickType.RELEASE, scanCode, modifiers);
        }

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
        RenderUtils.fillRect(context, mouseX, mouseY, (int)(width * textScale), 9, 0xFF000000);
        RenderUtils.drawText(context, name, mouseX + 2, mouseY + (int)(9 * 0.33), 0.7F, true);
    }

    public GuiElement getHoveredElement(double mouseX, double mouseY) {
        for (int i = children.size() - 1; i >= 0; i--) {
            GuiElement child = children.get(i);
            if (child.isContainer() ? child.isMouseOver((int)mouseX, (int)mouseY) : child.isHovered((int)mouseX, (int)mouseY)) {
                if (child.isContainer()) {
                    GuiElement deepChild = child.getHoveredElement(mouseX, mouseY);
                    return deepChild != null ? deepChild : child;
                }
                return child;
            }
        }
        return null;
    }
}
