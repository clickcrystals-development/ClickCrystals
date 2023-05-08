package io.github.itzispyder.clickcrystals.modules.settings;

public class BooleanSetting implements Setting<Boolean> {

    private Boolean val, def;
    private String name;

    private BooleanSetting() {

    }

    @Override
    public Boolean getVal() {
        return val;
    }

    @Override
    public Boolean getDef() {
        return def;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setVal(Boolean val) {
        this.val = val;
    }

    @Override
    public void setDef(Boolean def) {
        this.def = def;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Setting<Boolean> setting;

        public Builder() {
            this.setting = new BooleanSetting();
            this.setting.init("Boolean Setting", false);
        }

        public Builder name(String name) {
            setting.setName(name);
            return this;
        }

        public Builder def(boolean def) {
            setting.setDef(def);
            return this;
        }

        public Builder val(boolean val) {
            setting.setVal(val);
            return this;
        }

        public Setting<Boolean> build() {
            return setting;
        }
    }
}
