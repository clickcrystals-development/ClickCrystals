package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SettingSection implements SettingContainer {

    private final String name, id;
    private final List<ModuleSetting<?>> settings;

    public SettingSection(String name) {
        this.id = name;
        this.name = StringUtils.capitalizeWords(name);
        this.settings = new ArrayList<>();
    }

    public <T, S extends ModuleSetting<T>> S add(S setting) {
        this.settings.add(setting);
        return setting;
    }

    public void remove(ModuleSetting<?> setting) {
        this.settings.remove(setting);
    }

    public List<ModuleSetting<?>> getSettings() {
        return settings;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void forEach(Consumer<ModuleSetting<?>> action) {
        this.settings.forEach(action);
    }
}
