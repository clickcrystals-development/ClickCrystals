package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.modules.settings.SettingBuilder;
import io.github.itzispyder.clickcrystals.modules.settings.SettingChangeCallback;
import io.github.itzispyder.clickcrystals.util.StringUtils;

public abstract class ModuleSetting<T> {

    private SettingChangeCallback<ModuleSetting<T>> changeAction;
    private final String name, id, description;
    protected T def, val;

    protected ModuleSetting(String name, String description, T def, T val) {
        this.id = name;
        this.name = StringUtils.capitalizeWords(name);
        this.description = description;
        this.def = def;
        this.val = val;
        this.changeAction = setting -> {};
    }
    protected ModuleSetting(String name, String description, T val) {
        this(name, description, val, val);
    }

    public abstract <E extends GuiElement> E toGuiElement(int x, int y);

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

    public void setVal(Object val) {
        this.val = (T)val;
        this.changeAction.onChange(this);
    }

    public String getId() {
        return id;
    }

    public SettingChangeCallback<ModuleSetting<T>> getChangeAction() {
        return changeAction;
    }

    public void setChangeAction(SettingChangeCallback<ModuleSetting<T>> changeAction) {
        this.changeAction = changeAction;
    }

    public class Builder extends SettingBuilder<T, Builder, ModuleSetting<T>> {
        @Override
        public ModuleSetting<T> buildSetting() {
            return null;
        }
    }
}
