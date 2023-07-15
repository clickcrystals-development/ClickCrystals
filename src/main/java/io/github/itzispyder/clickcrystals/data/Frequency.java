package io.github.itzispyder.clickcrystals.data;

import io.github.itzispyder.clickcrystals.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

public enum Frequency {

    LOWEST(6),
    LOWER(5),
    LOW(4),
    NORMAL(3),
    HIGH(2),
    HIGHER(1),
    HIGHEST(0);

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
        for (Frequency f : values()) {
            if (f.value == value) {
                return f;
            }
        }
        return null;
    }
}
