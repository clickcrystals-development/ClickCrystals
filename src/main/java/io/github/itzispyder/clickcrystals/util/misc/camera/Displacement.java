package io.github.itzispyder.clickcrystals.util.misc.camera;

public class Displacement {

    public final float displacement;
    public final int direction;

    private long timeEnd;
    private float timeLen;
    private float timeLenCoefficient;

    // direction is -1, 0, 1
    // sensitivity in degrees per tick
    public Displacement(float displacement, int direction, float sensitivity) {
        this.displacement = displacement;
        this.direction = direction;
        this.timeLen = estimateFinishTime(sensitivity);
        this.timeEnd = System.currentTimeMillis() + (long)(timeLen * 50L);
        this.timeLenCoefficient = 1 / (timeLen * 50);
    }

    public void setSensitivity(float sensitivity) {
        this.timeLen = estimateFinishTime(sensitivity);
        this.timeEnd = System.currentTimeMillis() + (long)(timeLen * 50L);
        this.timeLenCoefficient = 1 / (timeLen * 50);
    }

    // remember these are degrees not radians
    public static Displacement fromAngles(float angle1, float angle2, float sensitivity) {
        float a = angle1 % 360;
        float b = angle2 % 360;
        float dSigned = b - a;

        if (dSigned > 180)
            dSigned -= 360;
        else if (dSigned < -180)
            dSigned += 360;

        float displacement = Math.abs(dSigned);
        int direction = (int) Math.signum(dSigned);
        return new Displacement(displacement, direction, sensitivity);
    }

    public float getProgress() {
        return 1 - (timeEnd - System.currentTimeMillis()) * timeLenCoefficient;
    }

    // returns milliseconds time length
    private float estimateFinishTime(float sensitivity) {
        return displacement / sensitivity;
    }

    public float getTimeLen() {
        return timeLen;
    }
}
