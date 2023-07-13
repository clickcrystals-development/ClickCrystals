package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;

public class StringSetting extends Setting<String> {

    public StringSetting(String name, String description, String def, String val) {
        super(name, description, def, val);
    }

    @Override
    public <E extends GuiElement> E toGuiElement(int x, int y, int width, int height) {
        return null;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<String> {
        @Override
        public Setting<String> build() {
            return new StringSetting(name, description, def, getOrDef(val, def));
        }
    }
}
