package io.github.itzispyder.clickcrystals.data;

import java.io.Serializable;

/**
 * Configuration segment for configuration files
 * @param <O> segment type
 */
public class ConfigSegment<O extends Serializable> implements Serializable {

    private final O object;

    /**
     * Constructs a configuration segment for a configuration file
     * @param object object
     */
    public ConfigSegment(final O object) {
        this.object = object;
    }

    /**
     * Returns the object saved
     * @return object
     */
    public O get() {
        return object;
    }
}
