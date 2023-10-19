package io.github.itzispyder.clickcrystals.gui_beta.elements.client.module;

import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.Typeable;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class StringSettingElement extends SettingElement<StringSetting> implements Typeable {

    private final StringSetting setting;
    private String input;

    public StringSettingElement(StringSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        this.input = setting.getVal();
        createResetButton();
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);

        if (mc.currentScreen instanceof GuiScreen screen) {
            int drawW = width / (screen.selected == this ? 2 : 4);
            int drawH = 12;
            int drawY = y + height / 2;
            int drawX = x + width - drawW - 5;

            Gray fill = screen.selected == this ? Gray.LIGHT_GRAY : Gray.GRAY;
            RoundRectBrush.drawRoundHoriLine(context, drawX, drawY, drawW, drawH, fill);

            String text = input;
            while (text.length() > 0 && mc.textRenderer.getWidth(text) * 0.7F > drawW - 10) {
                text = text.substring(1);
            }

            String displayText = screen.selected == this ? text + "§8§l︳" : text;
            RenderUtils.drawText(context, displayText, drawX + 5, drawY + drawH / 3, 0.7F, false);
        }
    }

    @Override
    public void revertSettingValue() {
        this.input = setting.getDef();
        this.setting.setVal(input);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            screen.selected = this;
        }

        setting.setVal(input);
    }

    @Override
    public void onKey(int key, int scancode) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            String typed = GLFW.glfwGetKeyName(key, scancode);

            if (key == GLFW.GLFW_KEY_ESCAPE) {
                screen.selected = null;
            }
            else if (key == GLFW.GLFW_KEY_BACKSPACE && !input.isEmpty()) {
                input = input.substring(0, input.length() - 1);
            }
            else if (key == GLFW.GLFW_KEY_SPACE) {
                input = input.concat(" ");
            }
            else if (key == GLFW.GLFW_KEY_V && screen.ctrlKeyPressed) {
                input = input.concat(StringUtils.fromClipboard());
            }
            else if (typed != null){
                input = input.concat(screen.shiftKeyPressed ? StringUtils.keyPressWithShift(typed) : typed);
            }

            setting.setVal(input);
        }
    }

    public String getInput() {
        return input;
    }

    public StringSetting getSetting() {
        return setting;
    }
}
