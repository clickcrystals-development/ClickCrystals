package io.github.itzispyder.clickcrystals.gui.hud;

public abstract class RelativeHud extends Hud {

    private double rx, ry, drx, dry;

    public RelativeHud(double rx, double ry, double drx, double dry, int width, int height) {
        super(0, 0, width, height);
        this.rx = rx;
        this.ry = ry;
        this.drx = drx;
        this.dry = dry;
    }

    public RelativeHud(double drx, double dry, int width, int height) {
        this(drx, dry, drx, dry, width, height);
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
    public void revertDimensions() {
        this.rx = drx;
        this.ry = dry;
    }
}
