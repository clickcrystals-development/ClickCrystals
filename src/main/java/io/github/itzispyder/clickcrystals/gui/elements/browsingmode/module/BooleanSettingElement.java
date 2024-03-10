package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class BooleanSettingElement extends SettingElement<BooleanSetting> {

    public BooleanSettingElement(BooleanSetting setting, int x, int y) {
        super(setting, x, y);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        int drawY = y + height / 2;
        int drawX = x + width - 20 - 5;

        // custom
        if (setting.getVal()) {
            RenderUtils.fillRoundHoriLine(context, drawX, drawY, 20, 6, Shades.GENERIC_LOW);
            RenderUtils.fillCircle(context, drawX + 17, drawY + 3, 5, Shades.GENERIC);
        }
        else {
            RenderUtils.fillRoundHoriLine(context, drawX, drawY, 20, 6, Shades.GRAY);
            RenderUtils.fillCircle(context, drawX + 3, drawY + 3, 5, Shades.LIGHT_GRAY);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (isHovered((int)mouseX, (int)mouseY)) {
            setting.setVal(!setting.getVal());
        }
    }

    public BooleanSetting getSetting() {
        return setting;
    }
}
