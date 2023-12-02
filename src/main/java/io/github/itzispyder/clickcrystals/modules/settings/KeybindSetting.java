package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.client.module.KeybindSettingElement;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.BindCondition;
import io.github.itzispyder.clickcrystals.modules.keybinds.KeyAction;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;

public class KeybindSetting extends ModuleSetting<Keybind> {

    public final Keybind bind;

    public KeybindSetting(String name, String description, Keybind bind) {
        super(name, description, bind);
        this.bind = bind;
    }

    public int getKey() {
        return bind.getKey();
    }

    public void setKey(int val) {
        bind.setKey(val);
    }

    public int getDefKey() {
        return bind.getDefaultKey();
    }

    public void setDefKey(int def) {
        bind.setDefaultKey(def);
    }

    public Keybind getBind() {
        return bind;
    }

    @Override
    public KeybindSettingElement toGuiElement(int x, int y) {
        return new KeybindSettingElement(this, x, y);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Keybind, Builder, KeybindSetting> {

        private String id;
        private int key, defaultKey;
        private KeyAction keyAction;
        private BindCondition bindCondition;

        public Builder() {
            id = "unregistered-keybind";
            key = defaultKey = Keybind.NONE;
            keyAction = bind -> {};
            bindCondition = (bind, screen) -> true;
        }

        public Builder val(int key) {
            this.key = key;
            return this;
        }

        public Builder def(int defaultKey) {
            this.defaultKey = defaultKey;
            return this;
        }

        @Override
        public Builder name(String id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder onPress(KeyAction keyAction) {
            this.keyAction = keyAction;
            return this;
        }

        public Builder condition(BindCondition bindCondition) {
            this.bindCondition = bindCondition;
            return this;
        }

        @Override
        public KeybindSetting build() {
            key = key == Keybind.NONE ? defaultKey : key;
            return new KeybindSetting(id, description, Keybind.create()
                    .id(id)
                    .key(key)
                    .defaultKey(defaultKey)
                    .onPress(keyAction)
                    .condition(bindCondition)
                    .build()
            );
        }
    }
}
