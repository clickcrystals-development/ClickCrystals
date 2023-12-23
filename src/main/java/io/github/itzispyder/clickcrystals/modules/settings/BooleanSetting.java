package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.BooleanSettingElement;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;

public class BooleanSetting extends ModuleSetting<Boolean> {

    public BooleanSetting(String name, String description, boolean def, boolean val) {
        super(name, description, def, val);
    }

    @Override
    public BooleanSettingElement toGuiElement(int x, int y) {
        return new BooleanSettingElement(this, x, y);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Boolean, Builder, BooleanSetting> {
        @Override
        public BooleanSetting buildSetting() {
            return new BooleanSetting(name, description, def, getOrDef(val, def));
        }
    }
}
