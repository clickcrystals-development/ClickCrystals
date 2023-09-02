package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;

public abstract class NumberSetting<T extends Number> extends ModuleSetting<T> {

    protected T min, max;

    public NumberSetting(String name, String description, T def, T val, T min, T max) {
        super(name, description, def, val);
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }
}
