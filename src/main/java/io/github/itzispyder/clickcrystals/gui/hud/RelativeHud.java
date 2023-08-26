package io.github.itzispyder.clickcrystals.gui.hud;

public abstract class RelativeHud extends Hud {

    private double rx, ry, drx, dry;

    public RelativeHud(String id, double rx, double ry, double drx, double dry, int width, int height) {
        super(id, 0, 0, width, height);
        this.rx = rx;
        this.ry = ry;
        this.drx = drx;
        this.dry = dry;
    }

    public RelativeHud(String id, double drx, double dry, int width, int height) {
        this(id, drx, dry, drx, dry, width, height);
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public double getDrx() {
        return drx;
    }

    public void setDrx(double drx) {
        this.drx = drx;
    }

    public double getDry() {
        return dry;
    }

    public void setDry(double dry) {
        this.dry = dry;
    }

    @Override
    public int getX() {
        return (int)(getWindow().getScaledWidth() * rx);
    }

    @Override
    public int getY() {
        return (int)(getWindow().getScaledHeight() * ry);
    }

    @Override
    public void setX(int x) {
        rx = x / (double)getWindow().getScaledWidth();
        super.setX(getX());
    }

    @Override
    public void setY(int y) {
        ry = y / (double)getWindow().getScaledHeight();
        super.setY(getY());
    }

    @Override
    public void setDimensions(Dimension dim) {
        setX(dim.x);
        setY(dim.y);
        setWidth(dim.width);
        setHeight(dim.height);
    }

    @Override
    public void revertDimensions() {
        this.rx = drx;
        this.ry = dry;
    }
}
