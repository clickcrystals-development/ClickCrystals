package io.github.itzispyder.clickcrystals.gui;

public interface Positionable {

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    int getWidth();

    void setWidth(int width);

    int getHeight();

    void setHeight(int height);

    default int[] getDimensions() {
        return new int[]{getX(), getY(), getWidth(), getHeight()};
    }
}
