package io.github.itzispyder.clickcrystals.modules.settings;

public class DoubleSetting extends NumberSetting<Double> {

    private int decimalPlaces;

    public DoubleSetting(String name, String description, double def, double val, double min, double max, int decimalPlaces) {
        super(name, description, def, val, min, max);
        this.decimalPlaces = Math.max(1, decimalPlaces);
    }

    @Override
    public void setMax(Double max) {
        super.setMax(Math.max(min + 1, max));
    }

    @Override
    public void setMin(Double min) {
        super.setMin(Math.min(min, max - 1));
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
        double ex = 10 ^ decimalPlaces;
        return Math.floor(val * ex) / ex;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Double> {

        private double min, max;
        private int decimalPlaces;

        public Builder() {
            this.min = 0;
            this.max = 1;
        }

        public Builder min(double min) {
            this.min = Math.min(min, max - 1);
            return this;
        }

        public Builder max(double max) {
            this.max = Math.max(min + 1, max);
            return this;
        }

        public Builder decimalPlaces(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
            return this;
        }

        @Override
        public Setting<Double> build() {
            return new DoubleSetting(name, description, def, val, min, max, decimalPlaces);
        }
    }
}
