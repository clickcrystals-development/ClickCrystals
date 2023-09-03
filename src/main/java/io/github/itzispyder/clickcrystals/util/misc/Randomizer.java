package io.github.itzispyder.clickcrystals.util.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Randomize items from a list
 * @param <T> list of?
 */
public class Randomizer<T> {

    private final List<T> array;

    /**
     * From array list
     * @param array list
     */
    public Randomizer(List<T> array) {
        this.array = array;
    }

    /**
     * From set
     * @param array set
     */
    public Randomizer(Set<T> array) {
        this.array = new ArrayList<>(array);
    }

    /**
     * From array
     * @param array array
     */
    public Randomizer(T[] array) {
        this.array = List.of(array);
    }

    /**
     * Pick random from the array
     * @return random of list of?
     */
    public T pickRand() {
        return array.get(rand(array.size()) - 1);
    }

    /**
     * Generates a random integer from 1 to (max)
     * @param max max value
     * @return random
     */
    public static int rand(int max) {
        if (max <= 0) throw new IllegalArgumentException("max cannot be less than 1!");
        return (int) Math.ceil(Math.random() * max);
    }

    /**
     * Generates a random integer from (min) to (max)
     * @param min min value
     * @param max max value
     * @return random
     */
    public static int rand(int min, int max) {
        if (max <= 0 || min <= 0) throw new IllegalArgumentException("max or min cannot be less than 1!");
        if (max <= min) throw new IllegalArgumentException("max cannot be less than or equal to min!");
        return min + (int) Math.floor(Math.random() * (max - min + 1));
    }
}
