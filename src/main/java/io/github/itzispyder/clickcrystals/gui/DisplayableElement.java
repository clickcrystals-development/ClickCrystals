package io.github.itzispyder.clickcrystals.gui;

import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public abstract class DisplayableElement implements Draggable {

    private int x, y, width, height;
    private int borderThickness, borderColor, fillColor;
    private boolean visible, dragging;
    private final List<DisplayableElement> children;
    private PressAction pressAction;

    public DisplayableElement() {
        this(0, 0, 0, 0, 1, 0xFF1A9494, 0xD0000000);
    }

    public DisplayableElement(int x, int y, int width, int height) {
        this(x, y, width, height, 1, 0xFF1A9494, 0xD0000000);
    }

    public DisplayableElement(int x, int y, int width, int height, int borderThickness) {
        this(x, y, width, height, borderThickness, 0xFF1A9494, 0xD0000000);
    }

    public DisplayableElement(int borderColor, int fillColor) {
        this(0, 0, 0, 0, 1, borderColor, fillColor);
    }

    public DisplayableElement(int x, int y, int width, int height, int borderThickness, int borderColor, int fillColor) {
        this.children = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.visible = true;
    }

    public void render(MatrixStack matrices, double mouseX, double mouseY) {
        if (!visible) return;

        DrawableHelper.fill(matrices, x, y, x + width, y + height, fillColor);
        DrawableUtils.drawBorder(matrices, x, y, width, height, borderThickness, borderColor);

        this.children.forEach(child -> {
            child.render(matrices, mouseX, mouseY);
        });
    }

    public void move(int deltaX, int deltaY) {
        this.setX(getX() + deltaX);
        this.setY(getY() + deltaY);

        this.children.forEach(child -> {
            child.move(deltaX, deltaY);
        });
    }

    public void moveTo(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public void move(double deltaX, double deltaY) {
        this.setX((int)(getX() + deltaX));
        this.setY((int)(getY() + deltaY));

        this.children.forEach(child -> {
            child.move(deltaX, deltaY);
        });
    }

    public void onClick(double mouseX, double mouseY, int button) {
        if (!visible) return;
        if (!isMouseOver(mouseX, mouseY)) return;
        if (pressAction == null) return;

        this.pressAction.onPress(this);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return isMouseOver(mouseX, mouseY, true);
    }

    public boolean isMouseOver(double mouseX, double mouseY, boolean withChildren) {
        boolean over = visible && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        if (over) return true;

        if (withChildren) {
            for (DisplayableElement child : this.children) {
                if (child.isMouseOver(mouseX, mouseY)) return true;
            }
        }

        return false;
    }

    public void moveTo(double x, double y) {
        this.moveTo((int)x, (int)y);
    }

    public void addChild(DisplayableElement child) {
        this.children.add(child);
    }

    public void removeChild(DisplayableElement child) {
        this.children.remove(child);
    }

    @Override
    public List<DisplayableElement> getDraggableChildren() {
        return children;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public PressAction getPressAction() {
        return pressAction;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPressAction(PressAction pressAction) {
        this.pressAction = pressAction;
    }
}
