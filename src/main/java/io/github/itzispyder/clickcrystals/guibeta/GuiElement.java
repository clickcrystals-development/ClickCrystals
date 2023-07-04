package io.github.itzispyder.clickcrystals.guibeta;

import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GuiElement {

    public int x, y, width, height;
    public boolean rendering, draggable;
    private GuiElement parent;
    private final List<GuiElement> children;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rendering = true;
        this.draggable = false;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        if (rendering) {
            onRender(context, mouseX, mouseY);

            this.children.forEach(guiElement -> {
                guiElement.render(context, mouseX, mouseY);
            });
        }
    }

    public abstract void onRender(DrawContext context, int mouseX, int mouseY);

    public abstract void onClick(double mouseX, double mouseY);

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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

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
}
