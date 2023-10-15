package io.github.itzispyder.clickcrystals.gui_beta.elements.client.module;

import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.Typeable;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

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
            Gray fill = screen.selected == this ? Gray.LIGHT_GRAY : Gray.GRAY;
            RoundRectBrush.drawRoundRect(context, drawX, drawY, drawW, drawH, 3, fill);

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
            setting.setKey(key);
            currentScanCode = scanCode;
            screen.selected = null;
        }
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

        name = name != null ? "§7[§f" + name.toUpperCase() + "§7]" : "§7NONE";
        display = name;
    }
}
