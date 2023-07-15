package io.github.itzispyder.clickcrystals.data;

import io.github.itzispyder.clickcrystals.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

public enum Frequency {

    LOWEST(0),
    LOWER(5),
    LOW(10),
    NORMAL(15),
    HIGH(20),
    HIGHER(25),
    HIGHEST(30);

    private int value;

    Frequency(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Frequency fromList(Iterable<Frequency> frequencies) {
        List<Integer> ints = new ArrayList<>();
        for (Frequency f : frequencies) {
            ints.add(f.value);
        }
        return fromValue((int)MathUtils.avg(ints));
    }

    public static Frequency fromValue(int value) {
        int add = value % 5 != 0 ? 1 : 0;
        int val = (int)Math.floor(value / 5.0) + add;
        val *= 5;

        for (Frequency f : values()) {
            if (val == f.value) {
                return f;
            }
        }
        return null;
    }
}
