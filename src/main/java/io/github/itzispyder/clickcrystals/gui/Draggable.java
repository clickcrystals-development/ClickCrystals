package io.github.itzispyder.clickcrystals.gui;

import io.github.itzispyder.clickcrystals.data.Delta3d;
import net.minecraft.client.util.Window;

import java.util.List;
import java.util.function.Consumer;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public interface Draggable {

    boolean canDrag();

    <T extends Draggable> List<T> getDraggableChildren();

    int getX();

    int getY();

    int getWidth();

    int getHeight();

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

    default boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    default void dragWith(double mouseX, double mouseY) {
        dragWith(mouseX, mouseY, false);
    }

    default void dragWith(double mouseX, double mouseY, boolean force) {
        if (!force && !canDrag()) return;

        int delX = (int)(mouseX - getX());
        int delY = (int)(mouseY - getY());

        moveTo(mouseX, mouseY);

        forEachDraggableChild(child -> {
            child.move(delX, delY);
        });
    }

    default void dragAlong(Delta3d predrag, Delta3d postdrag) {
        dragAlong(predrag, postdrag, false);
    }

    default void dragAlong(Delta3d predrag, Delta3d postdrag, boolean force) {
        if (!force && !canDrag()) return;

        double delX = predrag.x() - getX();
        double delY = predrag.y() - getY();

        moveTo(postdrag.x(), postdrag.y());
        move(-delX, -delY);

        forEachDraggableChild(child -> {
            child.dragAlong(predrag, postdrag, true);
        });
    }

    default boolean checkBounds() {
        final Window win = mc.getWindow();
        int winWidth = win.getScaledWidth();
        int winHeight = win.getScaledHeight();

        if (getX() + getWidth() > winWidth) {
            setX(winWidth - getWidth());
            return false;
        }
        if (getX() < 0) {
            setX(0);
            return false;
        }
        if (getY() + getHeight() > winHeight) {
            setY(winHeight - getHeight());
            return false;
        }
        if (getY() < 0) {
            setY(0);
            return false;
        }

        return true;
    }

    default void dragWith(int mouseX, int mouseY) {
        dragWith((double)mouseX, (double)mouseY);
    }
}
