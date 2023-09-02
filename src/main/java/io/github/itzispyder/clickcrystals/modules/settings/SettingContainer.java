package io.github.itzispyder.clickcrystals.modules.settings;

public interface SettingContainer {

    default IntegerSetting.Builder createIntSetting() {
        return IntegerSetting.create();
    }

    default DoubleSetting.Builder createDoubleSetting() {
        return DoubleSetting.create();
    }

    default BooleanSetting.Builder createBoolSetting() {
        return BooleanSetting.create();
    }

    default StringSetting.Builder createStringSetting() {
        return StringSetting.create();
    }

    default KeybindSetting.Builder createBindSetting() {
        return KeybindSetting.create();
    }

    default <T extends Enum<?>> EnumSetting.Builder<T> createEnumSetting(Class<T> type) {
        return EnumSetting.create(type);
    }
}
