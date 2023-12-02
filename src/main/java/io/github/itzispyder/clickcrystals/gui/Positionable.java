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

    default Dimension getDimensions() {
        return new Dimension(getX(), getY(), getWidth(), getHeight());
    }

    default void setDimensions(Dimension dim) {
        setX(dim.x);
        setY(dim.y);
        setWidth(dim.width);
        setHeight(dim.height);
    }

    default Dimension getCenter() {
        return new Dimension(getX() + getWidth() / 2, getY() + getHeight() / 2, 0, 0);
    }

    class Dimension {
        public int x, y, width, height;

        public Dimension() {
            this(0, 0, 0, 0);
        }

        public Dimension(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height= height;
        }

        public int getX() {
            return x;
        }

        public Dimension setX(int x) {
            this.x = x;
            return this;
        }

        public int getY() {
            return y;
        }

        public Dimension setY(int y) {
            this.y = y;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Dimension setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Dimension setHeight(int height) {
            this.height = height;
            return this;
        }
    }
}
