package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;

@FunctionalInterface
public interface SettingChangeCallback<T extends ModuleSetting<?>> {

    void onChange(T setting);
}
