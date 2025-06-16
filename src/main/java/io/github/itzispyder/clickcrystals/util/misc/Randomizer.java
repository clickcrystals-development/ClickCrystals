package io.github.itzispyder.clickcrystals.util.misc;

import java.util.List;

public class Randomizer {

    public Randomizer() {

    }

    public <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(getRandomIndex(list.size()));
    }

    @SafeVarargs
    public final <T> T getRandomElement(T... list) {
        if (list == null || list.length == 0) {
            return null;
        }
        return list[getRandomIndex(list.length)];
    }

    private <T> int getRandomIndex(int listSize) {
        if (listSize <= 0)
            listSize = 0;
        return (int)(Math.random() * listSize);
    }

    public int getRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max!");
        }
        int range = max - min + 1;
        return min + (int)(Math.random() * range);
    }

    public int getRandomInt(int max) {
        return getRandomInt(0, max);
    }

    public double getRandomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max!");
        }
        double range = max - min;
        return min + Math.random() * range;
    }

    public double getRandomDouble(double max) {
        return getRandomDouble(0.0, max);
    }

    public float getRandomFloat(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max!");
        }
        float range = max - min;
        return (float)(min + Math.random() * range);
    }

    public float getRandomFloat(float max) {
        return getRandomFloat(0.0F, max);
    }
}
