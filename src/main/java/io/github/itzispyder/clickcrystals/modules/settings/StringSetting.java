package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.client.module.StringSettingElement;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;

public class StringSetting extends ModuleSetting<String> {

    public StringSetting(String name, String description, String def, String val) {
        super(name, description, def, val);
    }

    @Override
    public StringSettingElement toGuiElement(int x, int y) {
        return new StringSettingElement(this, x, y);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<String, Builder, StringSetting> {
        @Override
        public StringSetting build() {
            return new StringSetting(name, description, def, getOrDef(val, def));
        }
    }
}
