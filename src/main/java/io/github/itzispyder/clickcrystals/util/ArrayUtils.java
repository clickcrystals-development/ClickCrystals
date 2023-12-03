package io.github.itzispyder.clickcrystals.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ArrayUtils {

    /**
     * Transforms an array to another one
     * @param e iterable list
     * @param a action
     * @return new transformed list
     * @param <I> input
     * @param <O> output
     */
    public static <I,O> List<O> toNewList(Iterable<I> e, Function<I,O> a) {
        List<O> list = new ArrayList<>();
        e.forEach(i -> list.add(a.apply(i)));
        return list;
    }

    public static List<String> enumNames(Class<? extends Enum<?>> enumType) {
        return Arrays.stream(enumType.getEnumConstants()).map(Enum::name).toList();
    }

    public static List<String> enumNamesLower(Class<? extends Enum<?>> enumType) {
        return Arrays.stream(enumType.getEnumConstants()).map(e -> e.name().toLowerCase()).toList();
    }

    public static boolean enumContainsStrict(Class<? extends Enum<?>> enumType, String name) {
        return enumNames(enumType).contains(name);
    }

    public static boolean enumContains(Class<? extends Enum<?>> enumType, String name) {
        return enumNamesLower(enumType).contains(name.toLowerCase());
    }

    public static <T> String list2string(List<T> list) {
        return "§7[§e" + String.join("§7, §e", ArrayUtils.toNewList(list, Object::toString)) + "§7]";
    }

    public static <T> List<T> bind(Iterable<T> tList, T... ts) {
        List<T> list = Arrays.asList(ts);
        tList.forEach(list::add);
        return list;
    }

    public static <T> List<T> reversed(List<T> input) {
        Collections.reverse(input);
        return input;
    }

    public static <T> List<T> reversed(Iterable<T> input) {
        List<T> list = new ArrayList<>();
        input.forEach(list::add);
        return reversed(list);
    }

    public static <T> void reverseForEach(Iterable<T> input, Consumer<T> action) {
        reversed(input).forEach(action);
    }
}
