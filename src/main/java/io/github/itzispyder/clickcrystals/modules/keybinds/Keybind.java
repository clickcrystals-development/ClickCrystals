package io.github.itzispyder.clickcrystals.modules.keybinds;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.ManualMap;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.HashMap;
import java.util.Map;

public class Keybind implements Global {

    public static final int DEFAULT_SCANCODE = 42;
    public static final int NONE = -1;
    public static final Map<Integer, String> EXTRAS = ManualMap.fromItems(
            InputConstants.KEY_LSHIFT, "LS",
            InputConstants.KEY_RSHIFT, "RS",
            InputConstants.KEY_LALT, "LA",
            InputConstants.KEY_RALT, "RA",
            InputConstants.KEY_LCONTROL, "LC",
            InputConstants.KEY_RCONTROL, "RC",
            InputConstants.KEY_PAGEDOWN, "P⇧",
            InputConstants.KEY_PAGEUP, "P⇩",
            InputConstants.KEY_UP, "⇧",
            InputConstants.KEY_DOWN, "⇩",
            InputConstants.KEY_LEFT, "⇦",
            InputConstants.KEY_RIGHT, "⇨"
    );
    public static final Map<Integer, String> EXTENDED_NAMES = ManualMap.fromItems(
            InputConstants.KEY_LSHIFT, "left_shift",
            InputConstants.KEY_RSHIFT, "right_shift",
            InputConstants.KEY_LALT, "left_alt",
            InputConstants.KEY_RALT, "right_alt",
            InputConstants.KEY_LCONTROL, "left_control",
            InputConstants.KEY_RCONTROL, "right_control",
            InputConstants.KEY_PAGEDOWN, "page_down",
            InputConstants.KEY_PAGEUP, "page_up",
            InputConstants.KEY_UP, "up_arrow",
            InputConstants.KEY_DOWN, "down_arrow",
            InputConstants.KEY_LEFT, "left_arrow",
            InputConstants.KEY_RIGHT, "right_arrow",
            InputConstants.KEY_ESCAPE, "escape",
            InputConstants.KEY_BACKSPACE, "backspace",
            InputConstants.KEY_INSERT, "insert",
            InputConstants.KEY_DELETE, "delete",
            InputConstants.KEY_HOME, "home",
            InputConstants.KEY_END, "end",
            InputConstants.KEY_TAB, "tab",
            InputConstants.KEY_CAPSLOCK, "capslock",
            InputConstants.KEY_SPACE, "space",
            InputConstants.KEY_RETURN, "enter"
    );
    private static final Map<Character, Integer> CHAR_KEYS = new HashMap<>();

    static {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < letters.length(); i++)
            CHAR_KEYS.put(letters.charAt(i), InputConstants.KEY_A + i);

        String digits = "1234567890";
        for (int i = 0; i < digits.length(); i++)
            CHAR_KEYS.put(digits.charAt(i), InputConstants.KEY_1 + i);

        CHAR_KEYS.put('-', InputConstants.KEY_MINUS);
        CHAR_KEYS.put('=', InputConstants.KEY_EQUALS);
        CHAR_KEYS.put('[', InputConstants.KEY_LBRACKET);
        CHAR_KEYS.put(']', InputConstants.KEY_RBRACKET);
        CHAR_KEYS.put('\\', InputConstants.KEY_BACKSLASH);
        CHAR_KEYS.put(';', InputConstants.KEY_SEMICOLON);
        CHAR_KEYS.put('\'', InputConstants.KEY_APOSTROPHE);
        CHAR_KEYS.put('`', InputConstants.KEY_GRAVE);
        CHAR_KEYS.put(',', InputConstants.KEY_COMMA);
        CHAR_KEYS.put('.', InputConstants.KEY_PERIOD);
        CHAR_KEYS.put('/', InputConstants.KEY_SLASH);
    }

    private static Character charOfKey(int key) {
        for (var entry : CHAR_KEYS.entrySet())
            if (entry.getValue() == key)
                return entry.getKey();
        return null;
    }

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
        if (bindCondition.meets(this, mc.gui.screen())) {
            keyAction.onKey(this);
        }
    }

    public boolean canPress(int keyCode, int scanCode) {
        boolean notNull = InputConstants.Type.KEYBOARD.getOrCreate(key) != InputConstants.UNKNOWN;
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
        Character ch = charOfKey(key);
        if (ch != null)
            return String.valueOf(ch);

        return EXTRAS.getOrDefault(key, InputConstants.Type.KEYBOARD.getOrCreate(key).getDisplayName().getString());
    }

    public static String getExtendedKeyName(int key, int scancode) {
        Character ch = charOfKey(key);
        if (ch != null)
            return String.valueOf(ch);

        return EXTENDED_NAMES.getOrDefault(key, InputConstants.Type.KEYBOARD.getOrCreate(key).getDisplayName().getString());
    }

    /**
     * Converts a key code saved by a pre 26.3 build (GLFW) into the SDL scancode used since 26.3.
     */
    public static int fromLegacyKey(int glfwKey) {
        if (glfwKey == NONE)
            return NONE;

        if (glfwKey >= 65 && glfwKey <= 90) // A-Z
            return InputConstants.KEY_A + (glfwKey - 65);

        if (glfwKey >= 48 && glfwKey <= 57) // 0-9
            return glfwKey == 48 ? InputConstants.KEY_0 : InputConstants.KEY_1 + (glfwKey - 49);

        if (glfwKey >= 290 && glfwKey <= 301) // F1-F12
            return InputConstants.KEY_F1 + (glfwKey - 290);

        if (glfwKey >= 302 && glfwKey <= 313) // F13-F24
            return InputConstants.KEY_F13 + (glfwKey - 302);

        if (glfwKey >= 320 && glfwKey <= 329) // keypad 0-9
            return glfwKey == 320 ? InputConstants.KEY_NUMPAD0 : InputConstants.KEY_NUMPAD1 + (glfwKey - 321);

        return switch (glfwKey) {
            case 32 -> InputConstants.KEY_SPACE;
            case 39 -> InputConstants.KEY_APOSTROPHE;
            case 44 -> InputConstants.KEY_COMMA;
            case 45 -> InputConstants.KEY_MINUS;
            case 46 -> InputConstants.KEY_PERIOD;
            case 47 -> InputConstants.KEY_SLASH;
            case 59 -> InputConstants.KEY_SEMICOLON;
            case 61 -> InputConstants.KEY_EQUALS;
            case 91 -> InputConstants.KEY_LBRACKET;
            case 92 -> InputConstants.KEY_BACKSLASH;
            case 93 -> InputConstants.KEY_RBRACKET;
            case 96 -> InputConstants.KEY_GRAVE;
            case 256 -> InputConstants.KEY_ESCAPE;
            case 257 -> InputConstants.KEY_RETURN;
            case 258 -> InputConstants.KEY_TAB;
            case 259 -> InputConstants.KEY_BACKSPACE;
            case 260 -> InputConstants.KEY_INSERT;
            case 261 -> InputConstants.KEY_DELETE;
            case 262 -> InputConstants.KEY_RIGHT;
            case 263 -> InputConstants.KEY_LEFT;
            case 264 -> InputConstants.KEY_DOWN;
            case 265 -> InputConstants.KEY_UP;
            case 266 -> InputConstants.KEY_PAGEUP;
            case 267 -> InputConstants.KEY_PAGEDOWN;
            case 268 -> InputConstants.KEY_HOME;
            case 269 -> InputConstants.KEY_END;
            case 280 -> InputConstants.KEY_CAPSLOCK;
            case 281 -> InputConstants.KEY_SCROLLLOCK;
            case 282 -> InputConstants.KEY_NUMLOCK;
            case 283 -> InputConstants.KEY_PRINTSCREEN;
            case 284 -> InputConstants.KEY_PAUSE;
            case 330 -> 99; // SDL_SCANCODE_KP_PERIOD
            case 331 -> 84; // SDL_SCANCODE_KP_DIVIDE
            case 332 -> InputConstants.KEY_MULTIPLY;
            case 333 -> 86; // SDL_SCANCODE_KP_MINUS
            case 334 -> InputConstants.KEY_ADD;
            case 335 -> InputConstants.KEY_NUMPADENTER;
            case 336 -> InputConstants.KEY_NUMPADEQUALS;
            case 340 -> InputConstants.KEY_LSHIFT;
            case 341 -> InputConstants.KEY_LCONTROL;
            case 342 -> InputConstants.KEY_LALT;
            case 343 -> InputConstants.KEY_LGUI;
            case 344 -> InputConstants.KEY_RSHIFT;
            case 345 -> InputConstants.KEY_RCONTROL;
            case 346 -> InputConstants.KEY_RALT;
            case 347 -> InputConstants.KEY_RGUI;
            case 348 -> 101; // SDL_SCANCODE_APPLICATION
            default -> NONE;
        };
    }

    public static int fromExtendedKeyName(String name) {
        for (var entry : EXTENDED_NAMES.entrySet())
            if (entry.getValue().equalsIgnoreCase(name))
                return entry.getKey();

        if (name.length() == 1) {
            Integer key = CHAR_KEYS.get(Character.toLowerCase(name.charAt(0)));
            return key != null ? key : NONE;
        }
        return NONE;
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
