package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.cc.settings.StringSettingElement;

public class StringSetting extends ModuleSetting<String> {

    public StringSetting(String name, String description, String def, String val) {
        super(name, description, def, val);
    }

    @Override
    public StringSettingElement toGuiElement(int x, int y, int width, int height) {
        return new StringSettingElement(this, x, y, width, height, 0.6F);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<String> {
        @Override
        public ModuleSetting<String> build() {
            return new StringSetting(name, description, def, getOrDef(val, def));
        }
    }
}
