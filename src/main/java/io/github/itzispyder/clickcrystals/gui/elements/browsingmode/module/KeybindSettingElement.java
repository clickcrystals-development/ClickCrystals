package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.Typeable;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.function.Function;

public class KeybindSettingElement extends SettingElement<KeybindSetting> implements Typeable {

    private final KeybindSetting setting;
    private String display;
    private int currentScanCode;

    public KeybindSettingElement(KeybindSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        this.display = null;
        this.currentScanCode = 42;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        int drawW = 20;
        int drawH = 12;
        int drawX = x + width - drawW - 5;
        int drawY = y + height / 2 - 2;

        if (mc.currentScreen instanceof GuiScreen screen) {
            int fill = screen.selected == this ? Shades.LIGHT_GRAY : Shades.GRAY;
            RenderUtils.fillRoundRect(context, drawX, drawY, drawW, drawH, 3, fill);

            updateDisplay();
            int cX = drawX + drawW / 2;
            int cY = drawY + drawH / 3;
            RenderUtils.drawCenteredText(context, display, cX, cY, 0.65F, false);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            screen.selected = this;
        }
    }

    @Override
    public void onKey(int key, int scanCode) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            setting.setKey(key == GLFW.GLFW_KEY_ESCAPE ? Keybind.NONE : key);
            currentScanCode = scanCode;
            screen.selected = null;
        }
    }

    @Override
    public void onInput(Function<String, String> factory) {

    }

    public String getDisplay() {
        return display;
    }

    public void updateDisplay() {
        int key = setting.getKey();
        String name = GLFW.glfwGetKeyName(key, currentScanCode);

        if (name == null || Keybind.EXTRAS.containsKey(key)) {
            name = Keybind.EXTRAS.get(key);
        }

        name = name != null && key != Keybind.NONE ? "§7[§f" + name.toUpperCase() + "§7]" : "§7NONE";
        display = name;
    }
}
