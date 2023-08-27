package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.cc.settings.IntegerSettingElement;
import io.github.itzispyder.clickcrystals.util.MathUtils;

public class IntegerSetting extends NumberSetting<Integer> {

    public IntegerSetting(String name, String description, int def, int val, int min, int max) {
        super(name, description, def, val, min, max);
    }

    @Override
    public IntegerSettingElement toGuiElement(int x, int y, int width, int height) {
        return new IntegerSettingElement(this, x, y, width, height);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Integer, Builder, IntegerSetting> {

        private int min, max;

        public Builder() {
            this.min = 0;
            this.max = 1;
        }

        public Builder min(int min) {
            this.min = Math.min(min, max - 1);
            return this;
        }

        public Builder max(int max) {
            this.max = Math.max(min + 1, max);
            return this;
        }

        @Override
        public IntegerSetting build() {
            return new IntegerSetting(name, description, MathUtils.minMax(def, min, max), getOrDef(val, def), min, max);
        }
    }
}
