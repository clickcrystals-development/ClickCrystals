package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.cc.settings.DoubleSettingElement;
import io.github.itzispyder.clickcrystals.util.MathUtils;

public class IntegerSetting extends DoubleSetting {

    public IntegerSetting(String name, String description, int def, int val, int min, int max) {
        super(name, description, def, val, min, max, 1);
    }

    @Override
    public DoubleSettingElement toGuiElement(int x, int y, int width, int height) {
        return new DoubleSettingElement(this, x, y, width, height);
    }

    public void setMax(Integer max) {
        super.setMax(Math.max(min + 1, max));
    }

    public void setMin(Integer min) {
        super.setMin(Math.min(min, max - 1));
    }

    public static Builder createInt() {
        return new Builder();
    }

    public int getValInt() {
        double d = super.val;
        return (int)d;
    }

    public int getDefInt() {
        double d = super.def;
        return (int)d;
    }

    public int getMinInt() {
        double d = super.min;
        return (int)d;
    }

    public int getMaxInt() {
        double d = super.max;
        return (int)d;
    }

    public static class Builder extends SettingBuilder<Double> {

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
        public Builder name(String name) {
            super.name = name;
            return this;
        }

        @Override
        public Builder description(String description) {
            super.description = description;
            return this;
        }

        @Override
        public Builder def(Double def) {
            super.def = def;
            return this;
        }

        @Override
        public Builder val(Double val) {
            super.val = val;
            return this;
        }

        public Builder def(double def) {
            super.def = def;
            return this;
        }

        public Builder val(double val) {
            super.val = val;
            return this;
        }

        public IntegerSetting build() {
            double set;
            set = val != null ? val : def;
            return new IntegerSetting(name, description, (int)MathUtils.minMax(def, min, max), (int)set, min, max);
        }
    }
}
