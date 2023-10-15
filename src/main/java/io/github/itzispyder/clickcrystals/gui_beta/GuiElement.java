package io.github.itzispyder.clickcrystals.gui_beta;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.GuiBorders;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiElement implements Positionable, Global {

    public int x, y, width, height;
    public boolean rendering, draggable, renderDependentOnParent;
    private GuiElement parent;
    private final List<GuiElement> children;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rendering = true;
        this.draggable = false;
        this.renderDependentOnParent = false;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        boolean bl = canRender();
        if (bl) {
            onRender(context, mouseX, mouseY);
        }

        for (GuiElement child : this.children) {
            child.render(context, mouseX, mouseY);
        }

        Module guiBorders = Module.get(GuiBorders.class);
        if (guiBorders.isEnabled()) {
            RenderUtils.drawBorder(context, x, y, width, height, 0xFFFFFFFF);
        }

        if (bl) {
            postRender(context, mouseX, mouseY);
        }
    }

    public abstract void onRender(DrawContext context, int mouseX, int mouseY);

    public abstract void onClick(double mouseX, double mouseY, int button);

    public void postRender(DrawContext context, int mouseX, int mouseY) {

    }

    public void onTick() {
        for (GuiElement child : children) {
            child.onTick();
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int)mouseX, (int)mouseY)) {
            onClick(mouseX, mouseY, button);
        }

        for (int i = getChildren().size() - 1; i >= 0; i--) {
            GuiElement child = getChildren().get(i);
            child.mouseClicked(mouseX, mouseY, button);
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return rendering && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return rendering && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
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
        if (child != null && child != this) {
            children.add(child);
        }
    }

    public void addChild(GuiElement parent, GuiElement child) {
        if (child != null && child != this && parent != null) {
            parent.addChild(child);
        }
    }

    public void removeChild(GuiElement child) {
        children.remove(child);
    }

    public GuiElement getParent() {
        return parent;
    }

    public void move(double deltaX, double deltaY) {
        this.x += deltaX;
        this.y += deltaY;

        this.children.forEach(child -> {
            child.move(deltaX, deltaY);
        });
    }

    public void moveTo(int x, int y) {
        int delX = x - this.x;
        int delY = y - this.y;

        this.x = x;
        this.y = y;

        this.children.forEach(child -> {
            child.move(delX, delY);
        });
    }

    public void resize(int width, int height) {
        double scaleW = (double)width / (double)this.width;
        double scaleH = (double)height / (double)this.height;

        this.width = width;
        this.height = height;

        this.children.forEach(child -> {
            child.width *= scaleW;
            child.height *= scaleH;
        });
    }

    public void scale(double scale) {
        width *= scale;
        height *= scale;

        this.children.forEach(child -> {
            child.scale(scale);
        });
    }

    public void centerIn(int frameWidth, int frameHeight) {
        moveTo(frameWidth / 2 - width / 2, frameHeight / 2 - height / 2);
    }

    public void boundIn(int frameWidth, int frameHeight) {
        int x = MathUtils.minMax(this.x, 0, frameWidth - width);
        int y = MathUtils.minMax(this.y, 0, frameHeight - height);
        moveTo(x, y);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isRendering() {
        return rendering;
    }

    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public void setParent(GuiElement parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean isRenderDependentOnParent() {
        return renderDependentOnParent;
    }

    public void setRenderDependentOnParent(boolean renderDependentOnParent) {
        this.renderDependentOnParent = renderDependentOnParent;
    }

    public boolean canRender() {
        if (rendering) {
            if (renderDependentOnParent && hasParent()) {
                return parent.canRender();
            }
            return true;
        }
        return false;
    }

    public void scrollOnPanel(ScrollPanelElement panel, int amount) {
        if (panel == null) return;

        setY(getY() + amount);

        for (GuiElement child : children) {
            child.scrollOnPanel(panel, amount);
        }
    }
}
