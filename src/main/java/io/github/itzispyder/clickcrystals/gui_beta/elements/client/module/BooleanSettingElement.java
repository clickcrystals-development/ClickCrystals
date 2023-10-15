package io.github.itzispyder.clickcrystals.gui_beta.elements.client.module;

import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class BooleanSettingElement extends SettingElement<BooleanSetting> {

    public BooleanSettingElement(BooleanSetting setting, int x, int y) {
        super(setting, x, y, 270, 30);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        int drawHeight = y + height / 2;

        // custom
        if (setting.getVal()) {
            RoundRectBrush.drawRoundHoriLine(context, x + width - 20 - 5, drawHeight, 20, 6, Gray.GENERIC_LOW);
            RenderUtils.drawTexture(context, Tex.Shapes.CIRCLE_BLUE, x + width - 10 - 5, drawHeight - 2, 10, 10);
        } else {
            RoundRectBrush.drawRoundHoriLine(context, x + width - 20 - 5, drawHeight, 20, 6, Gray.GRAY);
            RenderUtils.drawTexture(context, Tex.Shapes.CIRCLE_LIGHT_GRAY, x + width - 20 - 5, drawHeight - 2, 10, 10);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        setting.setVal(!setting.getVal());
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return rendering && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public BooleanSetting getSetting() {
        return setting;
    }
}
