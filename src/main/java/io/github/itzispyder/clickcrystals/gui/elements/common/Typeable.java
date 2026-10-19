package io.github.itzispyder.clickcrystals.gui.elements.common;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.function.Function;

public interface Typeable extends Global {

    default boolean onKey(int key, int scan) {
        if (!(mc.gui.screen() instanceof GuiScreen screen))
            return false;

        if (key == InputConstants.KEY_ESCAPE) {
            screen.selected = null;
            return true;
        }
        else if (key == InputConstants.KEY_BACKSPACE) {
            onInput(input -> input.isEmpty()
                    ? input
                    : input.substring(0, input.length() - 1));
            return true;
        }
        else if (key == InputConstants.KEY_V && screen.ctrlKeyPressed) {
            onInput(input -> input.concat(mc.keyboardHandler.getClipboard()));
            return true;
        }
        return false;
    }

    default void onChar(char chr) {
        if (!Character.isISOControl(chr))
            onInput(input -> input.concat(String.valueOf(chr)));
    }

    void onInput(Function<String, String> factory);
}
