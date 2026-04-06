package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.IntegerSettingElement;
import io.github.itzispyder.clickcrystals.util.MathUtils;

public class IntegerSetting extends NumberSetting<Integer> {

    public IntegerSetting(String name, String description, int def, int val, int min, int max) {
        super(name, description, def, val, min, max);
    }

    @Override
    public IntegerSettingElement toGuiElement(int x, int y) {
        return new IntegerSettingElement(this, x, y);
    }

    public static io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting.Builder create() {
        return new io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting.Builder();
    }

    public static class Builder extends SettingBuilder<Integer, io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting.Builder, IntegerSetting> {

        private int min, max;

        public Builder() {
            this.min = 0;
            this.max = 1;
        }

        public io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting.Builder min(int min) {
            this.min = Math.min(min, max);
            return this;
        }

        public io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting.Builder max(int max) {
            this.max = Math.max(min, max);
            return this;
        }

        @Override
        public IntegerSetting buildSetting() {
            return new IntegerSetting(name, description, MathUtils.clamp(def, min, max), getOrDef(val, def), min, max);
        }
    }
}
