package io.github.itzispyder.clickcrystals.modules.keybinds;

import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.ManualMap;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class Keybind {

    public static final int NONE = 256;
    public static final Map<Integer, String> EXTRAS = ManualMap.fromItems(
            GLFW.GLFW_KEY_LEFT_SHIFT, "LS",
            GLFW.GLFW_KEY_RIGHT_SHIFT, "RS",
            GLFW.GLFW_KEY_LEFT_ALT, "LA",
            GLFW.GLFW_KEY_RIGHT_ALT, "RA",
            GLFW.GLFW_KEY_LEFT_CONTROL, "LC",
            GLFW.GLFW_KEY_RIGHT_CONTROL, "RC",
            GLFW.GLFW_KEY_PAGE_DOWN, "P⇧",
            GLFW.GLFW_KEY_PAGE_UP, "P⇩",
            GLFW.GLFW_KEY_UP, "⇧",
            GLFW.GLFW_KEY_DOWN, "⇩",
            GLFW.GLFW_KEY_LEFT, "⇦",
            GLFW.GLFW_KEY_RIGHT, "⇨"
    );
    private final String name, id;
    private int key, defaultKey;
    private KeyAction keyAction, changeAction;
    private BindCondition bindCondition;

    public Keybind(String id, int defaultKey, int key, KeyAction keyAction, KeyAction changeAction, BindCondition bindCondition) {
        this.id = id;
        this.name = StringUtils.capitalizeWords(id);
        this.key = key;
        this.defaultKey = defaultKey;
        this.keyAction = keyAction;
        this.changeAction = changeAction;
        this.bindCondition = bindCondition;
        system.addKeybind(this);
    }

    public void onPress() {
        if (bindCondition.meets(this, mc.currentScreen)) {
            keyAction.onKey(this);
        }
    }

    public boolean canPress(int keyCode, int scanCode) {
        boolean notNull = GLFW.glfwGetKeyName(key, scanCode) != null;
        boolean isExtra = EXTRAS.containsKey(key);
        boolean isKey = keyCode == key;
        return (notNull || isExtra) && isKey;
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

    public String getKeyName() {
        String name = GLFW.glfwGetKeyName(key, 32);
        if (name == null) {
            name = EXTRAS.getOrDefault(key, "NONE");
        }
        return name;
    }

    public void setKey(int key) {
        this.key = key;
        this.changeAction.onKey(this);
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

    public KeyAction getChangeAction() {
        return changeAction;
    }

    public void setChangeAction(KeyAction changeAction) {
        this.changeAction = changeAction;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private int key, defaultKey;
        private KeyAction keyAction, changeAction;
        private BindCondition bindCondition;

        public Builder() {
            id = "unregistered-keybind";
            key = defaultKey = NONE;
            keyAction = changeAction = bind -> {};
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

        public Builder onChange(KeyAction changeAction) {
            this.changeAction = changeAction;
            return this;
        }

        public Builder condition(BindCondition bindCondition) {
            this.bindCondition = bindCondition;
            return this;
        }

        public Keybind build() {
            key = key == NONE ? defaultKey : key;
            return new Keybind(id, defaultKey, key, keyAction, changeAction, bindCondition);
        }
    }
}
