package io.github.itzispyder.clickcrystals.gui_beta.elements.client.module;

import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
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
            RoundRectBrush.drawRoundHoriLine(context, drawX, drawY, 20, 6, Gray.GENERIC_LOW);
            RenderUtils.drawTexture(context, Tex.Shapes.CIRCLE_BLUE, drawX + 10, drawY - 2, 10, 10);
        } else {
            RoundRectBrush.drawRoundHoriLine(context, drawX, drawY, 20, 6, Gray.GRAY);
            RenderUtils.drawTexture(context, Tex.Shapes.CIRCLE_LIGHT_GRAY, drawX, drawY - 2, 10, 10);
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
