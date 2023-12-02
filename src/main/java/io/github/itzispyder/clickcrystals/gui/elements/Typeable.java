package io.github.itzispyder.clickcrystals.gui.elements;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.function.Function;

public interface Typeable extends Global {

    default void onKey(int key, int scan) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            String typed = GLFW.glfwGetKeyName(key, scan);

            if (key == GLFW.GLFW_KEY_ESCAPE) {
                screen.selected = null;
            }
            else if (key == GLFW.GLFW_KEY_BACKSPACE) {
                onInput(input -> {
                    if (!input.isEmpty()) {
                        return input.substring(0, input.length() - 1);
                    }
                    return input;
                });
            }
            else if (key == GLFW.GLFW_KEY_SPACE) {
                onInput(input -> input.concat(" "));
            }
            else if (key == GLFW.GLFW_KEY_V && screen.ctrlKeyPressed) {
                onInput(input -> input.concat(mc.keyboard.getClipboard()));
            }
            else if (typed != null){
                onInput(input -> input.concat(screen.shiftKeyPressed ? StringUtils.keyPressWithShift(typed) : typed));
            }
        }
    }

    void onInput(Function<String, String> factory);
}
