package io.github.itzispyder.clickcrystals.data;

import java.io.Serializable;

/**
 * A section of a configuration file
 * @param <T> section object type
 */
public class ConfigSection<T extends Serializable> implements Serializable {

    private final T element;

    /**
     * Section constructor
     * @param element type
     */
    public ConfigSection(T element) {
        this.element = element;
    }

    /**
     * Returns object type
     * @return type
     */
    public T get() {
        return element;
    }

    public T getOrDefault(T def) {
        return get() != null ? get() : def;
    }
}
