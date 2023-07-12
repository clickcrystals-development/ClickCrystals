package io.github.itzispyder.clickcrystals.modules.settings;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, boolean def, boolean val) {
        super(name, description, def, val);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Boolean> {
        @Override
        public Setting<Boolean> build() {
            return new BooleanSetting(name, description, def, val);
        }
    }
}
