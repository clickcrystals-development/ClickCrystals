package io.github.itzispyder.clickcrystals.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class CCKeybindings {

    public static void init() {
        register(OPEN_MODULE);
    }

    private static void register(KeyBinding key) {
        KeyBindingHelper.registerKeyBinding(key);
    }

    public static class Keys {
        public static final String CATEGORY = "clickcrystals.category.main";
    }

    public static final KeyBinding OPEN_MODULE = new KeyBinding(
            "clickcrystals.key.open_modules",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            Keys.CATEGORY
    );
}
