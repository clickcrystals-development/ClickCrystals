package io.github.itzispyder.clickcrystals.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Make a map manually
 * @param <K> key
 * @param <V> value
 */
public class ManualMap<K,V> {

    public Map<K,V> map = new HashMap<>();

    /**
     * Manual map
     * @param objects objects array
     * @param <O> list of objects
     */
    public <O extends Object> ManualMap (O... objects) {
        if (objects.length % 2 != 0)
            throw new IllegalArgumentException("objects amount must be even for each key to have a value pair!");
        for (int i = 0; i < objects.length; i += 2) {
            try {
                map.put((K) objects[i],(V) objects[i + 1]);
            }
            catch (Exception ex) {
                map.clear();
                break;
            }
        }
    }

    /**
     * Returns the map
     * @return map
     */
    public Map<K, V> getMap() {
        return map;
    }
}
