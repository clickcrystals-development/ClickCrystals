package io.github.itzispyder.clickcrystals.gui.elements.common;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.function.Function;

public interface Typeable extends Global {

    default boolean onKey(int key, int scan) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                screen.selected = null;
                return true;
            }
            else if (key == GLFW.GLFW_KEY_BACKSPACE) {
                onInput(input -> {
                    if (!input.isEmpty()) {
                        return input.substring(0, input.length() - 1);
                    }
                    return input;
                });
                return true;
            }
            else if (key == GLFW.GLFW_KEY_V && screen.ctrlKeyPressed) {
                onInput(input -> input.concat(mc.keyboard.getClipboard()));
                return true;
            }
        }
        return false;
    }

    default void onChar(char chr, int modifiers) {
        if (Character.isISOControl(chr)) {
            return;
        }
        onInput(input -> input.concat(String.valueOf(chr)));
    }

    void onInput(Function<String, String> factory);
}
