package io.github.itzispyder.clickcrystals.modules.settings;

import java.io.Serializable;

@FunctionalInterface
public interface SettingChangeCallback<T extends ModuleSetting<?>> extends Serializable {

    void onChange(T setting);
}
