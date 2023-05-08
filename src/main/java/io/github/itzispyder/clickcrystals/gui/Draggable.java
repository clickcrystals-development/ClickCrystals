package io.github.itzispyder.clickcrystals.gui;

import java.util.List;
import java.util.function.Consumer;

public interface Draggable {

    boolean isDragging();

    <T extends Draggable> List<T> getDraggableChildren();

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setDragging(boolean dragging);

    void setX(int x);

    void setY(int y);

    void setWidth(int width);

    void setHeight(int height);

    default <T extends Draggable> void addDraggableChild(T child) {
        if (getDraggableChildren() == null) return;
        getDraggableChildren().add(child);
    }

    default <T extends Draggable> void removeDraggableChild(T child) {
        if (getDraggableChildren() == null) return;
        getDraggableChildren().remove(child);
    }

    default void forEachDraggableChild(Consumer<Draggable> action) {
        if (getDraggableChildren() == null) return;
        getDraggableChildren().forEach(action);
    }

    default void move(int delX, int delY) {
        setX(getX() + delX);
        setY(getY() + delY);
    }

    default void moveTo(int x, int y) {
        setX(x);
        setY(y);
    }

    default void setDimensions(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    default void move(double delX, double delY) {
        setX((int)(getX() + delX));
        setY((int)(getY() + delY));
    }

    default void moveTo(double x, double y) {
        setX((int)x);
        setY((int)y);
    }

    default void setDimensions(double width, double height) {
        setWidth((int)width);
        setHeight((int)height);
    }

    default void dragWith(double mouseX, double mouseY) {
        if (!isDragging()) return;

        int delX = (int)(mouseX - getX());
        int delY = (int)(mouseY - getY());

        forEachDraggableChild(child -> {
            child.move(delX, delY);
        });
        moveTo(mouseX, mouseY);
    }

    default void dragWith(int mouseX, int mouseY) {
        dragWith((double)mouseX, (double)mouseY);
    }
}
