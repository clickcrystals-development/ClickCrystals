package io.github.itzispyder.clickcrystals.util;

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
}
