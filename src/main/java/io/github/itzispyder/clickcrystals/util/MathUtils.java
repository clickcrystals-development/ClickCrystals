package io.github.itzispyder.clickcrystals.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class MathUtils {

    public static double avg(Integer... ints) {
        final List<Integer> list = Arrays.stream(ints).filter(Objects::nonNull).toList();
        return avg(list);
    }

    public static double avg(List<Integer> ints) {
        double sum = 0.0;
        for (Integer i : ints) sum += i;
        return sum / ints.size();
    }

    public static double round(double value, int nthPlace) {
        return Math.floor(value * nthPlace) / nthPlace;
    }

    public static int clamp(int val, int min, int max) {
        return Math.min(max, Math.max(min, val));
    }

    public static double clamp(double val, double min, double max) {
        return Math.min(max, Math.max(min, val));
    }

    public static double lerp(double a, double b, double delta) {
        return a + (b - a) * delta;
    }

    public static boolean oob(double val, double min, double max) {
        return val < min || val > max;
    }

    public static Vec3d forward(Vec3d pos, Vec3d dir, double dist) {
        return pos.add(dir.normalize().multiply(dist));
    }

    public static double floorDiff(double a) {
        return a - (int)a;
    }

    public static Vec3d lerpVec(double x1, double y1, double z1, double x2, double y2, double z2, float tickDelta) {
        return new Vec3d(
                x1 + (x2 - x1) * tickDelta,
                y1 + (y2 - y1) * tickDelta,
                z1 + (z2 - z1) * tickDelta
        );
    }

    public static Vec3d lerpEntityPosVec(Entity entity, float tickDelta) {
        return lerpVec(entity.lastRenderX, entity.lastRenderY, entity.lastRenderZ, entity.getX(), entity.getY(), entity.getZ(), tickDelta);
    }

    public static Vec3d lerpEntityEyeVec(LivingEntity entity, float tickDelta) {
        return lerpVec(entity.lastRenderX, entity.lastRenderY, entity.lastRenderZ, entity.getX(), entity.getY(), entity.getZ(), tickDelta).add(0, entity.getStandingEyeHeight(), 0);
    }

    public static float[] toPolar(double x, double y, double z) {
        double pi2 = 2 * Math.PI;
        float pitch, yaw;

        if (x == 0 && z == 0) {
            pitch = y > 0 ? -90 : 90;
            return new float[] { pitch, 0.0F };
        }

        double theta = Math.atan2(-x, z);
        yaw = (float)Math.toDegrees((theta + pi2) % pi2);

        double xz = Math.sqrt(x * x + z * z);
        pitch = (float)Math.toDegrees(Math.atan(-y / xz));

        return new float[] { pitch, yaw };
    }

    public static float cosInverse(double a) {
        return (float)Math.toDegrees(Math.acos(a));
    }

    public static float sinInverse(double a) {
        return (float)Math.toDegrees(Math.asin(a));
    }

    public static float tanInverse(double a) {
        return (float)Math.toDegrees(Math.atan(a));
    }

    public static boolean isWrapped(double deg) {
        double f = deg % 360.0;
        return f < 180 && f >= -180;
    }

    public static double wrapDegrees(double deg) {
        double f = deg % 360.0;

        if (f >= 180.0) {
            f -= 360.0;
        }
        if (f < -180.0) {
            f += 360.0;
        }

        return f;
    }

    public static double angleBetween(double deg1, double deg2) {
        double diff = deg2 - deg1;
        deg1 = wrapDegrees(deg1);
        deg2 = wrapDegrees(deg2);

        if ((deg1 < -90 && deg2 > 90) || (deg1 > 90 && deg2 < -90)) {
            return (180 - Math.abs(deg1)) + (180 - Math.abs(deg2));
        }
        if (diff >= 180 || diff < -180) {
            return Math.abs(deg2 - deg1);
        }
        return Math.abs(wrapDegrees(diff));
    }

    public static Vector3d toVector(float pitch, float yaw, float radius) {
        pitch = (float) Math.toRadians(pitch);
        yaw = (float) Math.toRadians(yaw);
        double x = radius * Math.cos(yaw) * Math.cos(pitch);
        double y = radius * Math.sin(pitch);
        double z = radius * Math.sin(yaw) * Math.cos(pitch);
        return new Vector3d(x, y, z);
    }

    public static float[] projectVertex(double x, double y, double z, float focalLen) {
        double depth = -(focalLen + z);
        if (depth >= 0)
            depth = -0.00000000001;
        float pixelX = (float) ((focalLen * x) / depth);
        float pixelY = (float) ((focalLen * y) / depth);
        return new float[] { pixelX, pixelY };
    }

    public static float[] projectVertex(Vector3d vector, Quaternionf rotation, float focalLen) {
        vector = rotation.transform(vector);
        return projectVertex(vector.x, vector.y, vector.z, focalLen);
    }

    public static float[] projectVertex(Vector3f vector, Quaternionf rotation, float focalLen) {
        vector = rotation.transform(vector);
        return projectVertex(vector.x, vector.y, vector.z, focalLen);
    }

    public static Vec3d rotate(Vec3d vec, Vec3d og, float pitch, float yaw) {
        Vector3f vector = vec.subtract(og).toVector3f();
        Quaternionf qYaw = new Quaternionf().rotationY((float)Math.toRadians(yaw));
        Quaternionf qPitch = new Quaternionf().rotationX((float)Math.toRadians(pitch));
        vector = qYaw.mul(qPitch).transform(vector).add(og.toVector3f());
        return new Vec3d(vector.x, vector.y, vector.z);
    }
}
