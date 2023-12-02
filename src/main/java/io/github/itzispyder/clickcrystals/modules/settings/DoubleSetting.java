package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.client.module.DoubleSettingElement;
import io.github.itzispyder.clickcrystals.util.MathUtils;

public class DoubleSetting extends NumberSetting<Double> {

    private int decimalPlaces;

    public DoubleSetting(String name, String description, double def, double val, double min, double max, int decimalPlaces) {
        super(name, description, def, val, min, max);
        this.decimalPlaces = Math.max(1, decimalPlaces);
    }

    @Override
    public DoubleSettingElement toGuiElement(int x, int y) {
        return new DoubleSettingElement(this, x, y);
    }

    @Override
    public void setMax(Double max) {
        super.setMax(Math.max(min, max));
    }

    @Override
    public void setMin(Double min) {
        super.setMin(Math.min(min, max));
    }

    @Override
    public void setDef(Double def) {
        super.setDef(round(def));
    }

    @Override
    public void setVal(Object val) {
        super.setVal(round((double)val));
    }

    @Override
    public Double getVal() {
        return round(super.getVal());
    }

    @Override
    public Double getDef() {
        return round(super.getDef());
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    private double round(double val) {
        int ex = MathUtils.exp(10, decimalPlaces);
        return Math.floor(val * ex) / ex;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Double, Builder, DoubleSetting> {

        private double min, max;
        private int decimalPlaces;

        public Builder() {
            this.min = 0;
            this.max = 1;
        }

        public Builder min(double min) {
            this.min = Math.min(min, max);
            return this;
        }

        public Builder max(double max) {
            this.max = Math.max(min, max);
            return this;
        }

        public Builder decimalPlaces(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
            return this;
        }

        @Override
        public DoubleSetting build() {
            return new DoubleSetting(name, description, MathUtils.clamp(def, min, max), getOrDef(val, def), min, max, decimalPlaces);
        }
    }
}
