package io.github.itzispyder.clickcrystals.util.misc;

public record Delta3d(double x, double y, double z) {

    public double distTo(Delta3d del) {
        double x = del.x - this.x;
        double y = del.y - this.y;
        double z = del.z - this.z;

        double xz = Math.sqrt(x * x + z * z);

        return Math.sqrt(xz * xz + y * y);
    }

    public double distSqr(Delta3d del) {
        double dist = distTo(del);
        return dist * dist;
    }

    public Delta3d multiply(double x, double y, double z) {
        return new Delta3d(this.x * x, this.y * y, this.z * z);
    }

    public Delta3d multiply(double val) {
        return multiply(val, val, val);
    }

    public Delta3d multiply(Delta3d del) {
        return multiply(del.x, del.y, del.z);
    }

    public Delta3d negate() {
        return multiply(-1);
    }

    public Delta3d add(double x, double y, double z) {
        return new Delta3d(this.x + x, this.y + y, this.z + z);
    }

    public Delta3d add(double val) {
        return add(val, val, val);
    }

    public Delta3d add(Delta3d del) {
        return add(del.x, del.y, del.z);
    }

    public Delta3d subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    public Delta3d subtract(double val) {
        return subtract(val, val, val);
    }

    public Delta3d subtract(Delta3d del) {
        return subtract(del.x, del.y, del.z);
    }

    public Delta3d clone()  {
        return new Delta3d(x, y, z);
    }
}
