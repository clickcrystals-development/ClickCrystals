package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.Typeable;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Function;

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
            int drawX = x + width - drawW - 7;

            int fill = screen.selected == this ? Shades.LIGHT_GRAY : Shades.GRAY;
            RenderUtils.fillRoundHoriLine(context, drawX, drawY, drawW, drawH, fill);

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
    public void onInput(Function<String, String> factory) {
        input = factory.apply(input);
        setting.setVal(input);
    }

    public String getInput() {
        return input;
    }

    public StringSetting getSetting() {
        return setting;
    }
}
