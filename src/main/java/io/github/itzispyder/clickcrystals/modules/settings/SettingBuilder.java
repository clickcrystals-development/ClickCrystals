package io.github.itzispyder.clickcrystals.modules.settings;

public abstract class SettingBuilder<T> {

    protected String name, description;
    protected T def, val;

    public SettingBuilder() {
        name = description = "";
        def = val = null;
    }

    protected <T> T getOrDef(T val, T def) {
        return val != null ? val : def;
    }

    public SettingBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public SettingBuilder<T> description(String description) {
        this.description = description;
        return this;
    }

    public SettingBuilder<T> def(T def) {
        this.def = def;
        return this;
    }

    public SettingBuilder<T> val(T val) {
        this.val = val;
        return this;
    }

    public abstract Setting<T> build();
}
