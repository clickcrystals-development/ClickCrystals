package io.github.itzispyder.clickcrystals.modules.keybinds;

import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.io.Serializable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class Keybind implements Serializable {

    private final String name, id;
    private int key, defaultKey;
    private KeyAction keyAction;
    private BindCondition bindCondition;

    public Keybind(String id, int defaultKey, int key, KeyAction keyAction, BindCondition bindCondition) {
        this.id = id;
        this.name = StringUtils.capitalizeWords(id);
        this.key = key;
        this.defaultKey = defaultKey;
        this.keyAction = keyAction;
        this.bindCondition = bindCondition;
        system.addKeybind(this);
    }

    public void onPress() {
        if (bindCondition.meets(this, mc.currentScreen)) {
            keyAction.onKey(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(int defaultKey) {
        this.defaultKey = defaultKey;
    }

    public KeyAction getKeyAction() {
        return keyAction;
    }

    public void setKeyAction(KeyAction keyAction) {
        this.keyAction = keyAction;
    }

    public BindCondition getBindCondition() {
        return bindCondition;
    }

    public void setBindCondition(BindCondition bindCondition) {
        this.bindCondition = bindCondition;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private int key, defaultKey;
        private KeyAction keyAction;
        private BindCondition bindCondition;

        public Builder() {
            id = "unregistered-keybind";
            key = defaultKey = 340;
            keyAction = bind -> {};
            bindCondition = (bind, screen) -> true;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder key(int key) {
            this.key = key;
            return this;
        }

        public Builder defaultKey(int defaultKey) {
            this.defaultKey = defaultKey;
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

        public Keybind build() {
            return new Keybind(id, defaultKey, key, keyAction, bindCondition);
        }
    }
}
