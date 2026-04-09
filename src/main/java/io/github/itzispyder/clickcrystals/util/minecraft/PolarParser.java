package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class PolarParser {

    private float pitch, yaw;

    public PolarParser(String arg1, String arg2) {
        arg1 = VectorParser.toNumber.apply(arg1);
        arg2 = VectorParser.toNumber.apply(arg2);
        float argPitch = 0.0F;
        float argYaw = 0.0F;

        if (!arg1.isEmpty()) {
            argPitch = Float.parseFloat(arg1);
        }
        if (!arg2.isEmpty()) {
            argYaw = Float.parseFloat(arg2);
        }

        this.pitch = argPitch;
        this.yaw = argYaw;
    }

    public PolarParser(String arg1, String arg2, Vec2 relativeTo) {
        arg1 = VectorParser.toRelation.apply(arg1);
        arg2 = VectorParser.toRelation.apply(arg2);
        float argPitch = 0.0F;
        float argYaw = 0.0F;

        if (!VectorParser.toNumber.apply(arg1).isEmpty()) {
            argPitch = Float.parseFloat(VectorParser.toNumber.apply(arg1));
        }
        if (!VectorParser.toNumber.apply(arg2).isEmpty()) {
            argYaw = Float.parseFloat(VectorParser.toNumber.apply(arg2));
        }

        if (arg1.startsWith("~")) {
            argPitch += relativeTo.x;
        }
        if (arg2.startsWith("~")) {
            argYaw += relativeTo.y;
        }

        this.pitch = argPitch;
        this.yaw = argYaw;
    }

    public PolarParser(String arg1, String arg2, Entity relativeTo) {
        this(arg1, arg2, relativeTo.getRotationVector());
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Vec2 getPolar() {
        return new Vec2(pitch, yaw);
    }

    public Vec3 getVector() {
        return Vec3.directionFromRotation(pitch, yaw);
    }
}
