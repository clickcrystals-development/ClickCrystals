package io.github.itzispyder.clickcrystals.util.misc;

import java.util.HashMap;
import java.util.Map;

public class ManualMap {

    public static <K, V> Map<K, V> fromEntries(Map.Entry<K, V>... entries) {
        return Map.ofEntries(entries);
    }

    public static <K, V> Map<K, V> fromPairs(Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs) {
            try {
                map.put(pair.left, pair.right);
            }
            catch (Exception ignore) {}
        }
        return map;
    }

    public static <K, V> Map<K, V> fromItems(Object... items) {
        if (items.length % 2 != 0) {
            throw new IllegalArgumentException("items amount must be even for each key to have a value!");
        }

        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < items.length; i += 2) {
            try {
                map.put((K)items[i], (V)items[i + 1]);
            }
            catch (Exception ex) {
                break;
            }
        }
        return map;
    }
}
