package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.io.Serializable;

public abstract class Setting<T> implements Serializable {

    private final String name, id, description;
    private T def, val;

    protected Setting(String name, String description, T def, T val) {
        this.id = name;
        this.name = StringUtils.capitalizeWords(name);
        this.description = description;
        this.def = def;
        this.val = val;
    }

    protected Setting(String name, String description, T val) {
        this(name, description, val, val);
    }

    public Class<T> getType() {
        return (Class<T>)val.getClass();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getDef() {
        return def;
    }

    public void setDef(T def) {
        this.def = def;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    public String getId() {
        return id;
    }

    public class Builder extends SettingBuilder<T> {
        @Override
        public Setting<T> build() {
            return null;
        }
    }
}
