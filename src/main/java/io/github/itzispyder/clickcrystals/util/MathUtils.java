package io.github.itzispyder.clickcrystals.util;

import java.time.LocalDateTime;
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

    public static int minMax(int val, int min, int max) {
        return Math.min(max, Math.max(min, val));
    }

    public static double minMax(double val, double min, double max) {
        return Math.min(max, Math.max(min, val));
    }

    public static float wrapDegrees(float deg) {
        float f = deg % 360.0F;

        if (f >= 180.0F) {
            f -= 360.0F;
        }
        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    public static float subtractDegrees(float deg1, float deg2) {
        return wrapDegrees(deg2 - deg1);
    }

    public static float angleBetween(float deg1, float deg2) {
        return Math.abs(subtractDegrees(deg1, deg2));
    }

    public static int exp(int val, int power) {
        int base = val;
        for (int i = 1; i < power; i++) {
            val *= base;
        }
        return val;
    }

    public static double exp(double val, double power) {
        double base = val;
        for (int i = 1; i < power; i++) {
            val *= base;
        }
        return val;
    }

    public static String getSystemLogTime() {
        LocalDateTime time = LocalDateTime.now();
        return twoDigitFormat(time.getHour()) + ":" + twoDigitFormat(time.getMinute()) + ":" + twoDigitFormat(time.getSecond());
    }

    public static String twoDigitFormat(int i) {
        return i < 10 && i >= 0 ? "0" + i : "" + i;
    }

    public static String twoDigitFormat(long i) {
        return i < 10L && i >= 0L ? "0" + i : "" + i;
    }
}
