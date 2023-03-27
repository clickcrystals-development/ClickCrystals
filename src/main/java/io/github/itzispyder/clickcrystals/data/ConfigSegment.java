package io.github.itzispyder.clickcrystals.data;

import java.io.Serializable;

/**
 * Configuration segment for configuration files
 * @param <O> segment type
 */
public class ConfigSegment<O extends Serializable> implements Serializable {

    private final O object;

    public ConfigSegment(final O object) {
        this.object = object;
    }

    public O get() {
        return object;
    }
}
