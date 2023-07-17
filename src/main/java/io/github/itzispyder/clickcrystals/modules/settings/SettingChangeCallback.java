package io.github.itzispyder.clickcrystals.modules.settings;

@FunctionalInterface
public interface SettingChangeCallback<T extends ModuleSetting<?>> {

    void onChange(T setting);
}
