package io.github.itzispyder.clickcrystals.modules.settings;

public abstract class Setting<T> {

    private String name, description;
    private T def, val;

    protected Setting(String name, String description, T def, T val) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public class Builder extends SettingBuilder<T> {
        @Override
        public Setting<T> build() {
            return null;
        }
    }
}
