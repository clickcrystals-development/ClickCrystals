package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class EnumSettingElement extends SettingElement<EnumSetting<?>> {

    private final EnumSetting<?> setting;

    public EnumSettingElement(EnumSetting<?> setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        createResetButton();
    }

    public EnumSetting<?> getSetting() {
        return setting;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        int drawY = y + height / 2;
        int drawX = x + width / 4 * 3 - 5;
        int drawW = width / 4;
        int drawH = 12;

        int fill = isHovered(mouseX, mouseY) ? Shades.LIGHT_GRAY : Shades.GRAY;
        RenderUtils.fillRoundHoriLine(context, drawX, drawY, drawW, drawH, fill);

        int cX = drawX + drawW / 2;
        int cY = drawY + drawH / 3;
        String display = setting.getVal().name();
        RenderUtils.drawCenteredText(context, display, cX, cY, 0.6F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        setting.next();
    }
}
