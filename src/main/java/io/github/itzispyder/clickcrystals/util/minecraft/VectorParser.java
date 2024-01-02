package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Function;
import java.util.function.Predicate;

public class VectorParser {

    private static final String REGEX_DECIMAL = "[^0-9 .-]";
    private static final String REGEX_RELATIVE = "[^0-9 ^~.-]";
    private static final Function<String, String> toNumber = s -> s.replaceAll(REGEX_DECIMAL, "").trim();
    private static final Function<String, String> toRelation = s -> s.replaceAll(REGEX_RELATIVE, "").trim();
    private static final Predicate<String> isRelative = s -> s.startsWith("~") || s.startsWith("^");
    private final double x, y, z;

    public VectorParser(String arg1, String arg2, String arg3) {
        arg1 = toNumber.apply(arg1);
        arg2 = toNumber.apply(arg2);
        arg3 = toNumber.apply(arg3);
        double argX = 0.0;
        double argY = 0.0;
        double argZ = 0.0;

        if (!arg1.isEmpty()) {
            argX = Double.parseDouble(arg1);
        }
        if (!arg2.isEmpty()) {
            argY = Double.parseDouble(arg2);
        }
        if (!arg3.isEmpty()) {
            argZ = Double.parseDouble(arg3);
        }

        this.x = argX;
        this.y = argY;
        this.z = argZ;
    }

    public VectorParser(String arg1, String arg2, String arg3, Vec3d relativeTo) {
        arg1 = toRelation.apply(arg1);
        arg2 = toRelation.apply(arg2);
        arg3 = toRelation.apply(arg3);
        double argX = 0.0;
        double argY = 0.0;
        double argZ = 0.0;

        if (!toNumber.apply(arg1).isEmpty()) {
            argX = Double.parseDouble(toNumber.apply(arg1));
        }
        if (!toNumber.apply(arg2).isEmpty()) {
            argY = Double.parseDouble(toNumber.apply(arg2));
        }
        if (!toNumber.apply(arg3).isEmpty()) {
            argZ = Double.parseDouble(toNumber.apply(arg3));
        }

        if (arg1.startsWith("~")) {
            argX += relativeTo.x;
        }
        if (arg2.startsWith("~")) {
            argY += relativeTo.y;
        }
        if (arg3.startsWith("~")) {
            argZ += relativeTo.z;
        }

        this.x = argX;
        this.y = argY;
        this.z = argZ;
    }

    public VectorParser(String arg1, String arg2, String arg3, Entity relativeTo) {
        this(arg1, arg2, arg3, relativeTo.getPos(), relativeTo.getRotationClient());
    }

    public VectorParser(String arg1, String arg2, String arg3, Vec3d relativePos, Vec2f relativeDir) {
        arg1 = toRelation.apply(arg1);
        arg2 = toRelation.apply(arg2);
        arg3 = toRelation.apply(arg3);
        double argX = 0.0;
        double argY = 0.0;
        double argZ = 0.0;
        String argNum1 = toNumber.apply(arg1);
        String argNum2 = toNumber.apply(arg2);
        String argNum3 = toNumber.apply(arg3);

        if (!argNum1.isEmpty()) {
            argX = Double.parseDouble(argNum1);
        }
        if (!argNum2.isEmpty()) {
            argY = Double.parseDouble(argNum2);
        }
        if (!argNum3.isEmpty()) {
            argZ = Double.parseDouble(argNum3);
        }

        Vec3d result = relativePos;

        if (arg1.startsWith("~")) {
            result = result.add(argX, 0, 0);
        }
        if (arg2.startsWith("~")) {
            result = result.add(0, argY, 0);
        }
        if (arg3.startsWith("~")) {
            result = result.add(0, 0, argZ);
        }

        float pitch = relativeDir.x;
        float yaw = relativeDir.y;

        if (arg1.startsWith("^")) {
            int angle = argX == 0 ? 0 : (argX < 0 ? -90 : 90);
            result = distInFront(result, 0, yaw + angle, Math.abs(argX));
        }
        if (arg2.startsWith("^")) {
            int angle = argY == 0 ? 0 : (argY < 0 ? 90 : -90);
            result = distInFront(result, pitch + angle, 0, Math.abs(argY));
        }
        if (arg3.startsWith("^")) {
            result = distInFront(result, pitch, yaw, argZ);
        }

        this.x = isRelative.test(arg1) ? result.x : argX;
        this.y = isRelative.test(arg2) ? result.y : argY;
        this.z = isRelative.test(arg3) ? result.z : argZ;
    }

    public static Vec3d distInFront(Vec3d start, Vec3d dir, double dist) {
        return start.add(dir.x * dist, dir.y * dist, dir.z * dist);
    }

    public static Vec3d distInFront(Vec3d start, float pitch, float yaw, double dist) {
        return distInFront(start, Vec3d.fromPolar(pitch, yaw), dist);
    }

    public Vec3d getVector() {
        return new Vec3d(x, y, z);
    }

    public BlockPos getBlockPos() {
        return new BlockPos((int)x, (int)y, (int)z);
    }

    public BlockState getBlock(World world) {
        return world.getBlockState(getBlockPos());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
