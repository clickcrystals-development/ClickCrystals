package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class IntegerSettingElement extends SettingElement<IntegerSetting> {

    private final Animator animator = new Animator(500, Animations.FADE_IN_AND_OUT);
    private boolean settingRenderUpdates;
    private int fillEnd;

    public IntegerSettingElement(IntegerSetting setting, int x, int y) {
        super(setting, x, y);
        this.settingRenderUpdates = true;
        this.fillEnd = x + width - 5;
        createResetButton();
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        int drawY = y + height / 2;
        int drawX = x + width / 4 * 3 - 5;
        int settingMin = setting.getMin();
        int settingMax = setting.getMax();

        int sliderEnd = x + width - 5;
        int sliderStart = drawX + 10;
        int sliderLen = sliderEnd - sliderStart;

        if (mc.currentScreen instanceof GuiScreen screen && screen.selected == this) {
            this.fillEnd = MathUtils.clamp(mouseX, sliderStart, sliderEnd);
            int range = settingMax - settingMin;
            double ratio = (double)(fillEnd - sliderStart) / (double)sliderLen;
            double value = range * ratio;
            setting.setVal((int)(value + settingMin));
        }

        double range = settingMax - settingMin;
        double value = setting.getVal() - settingMin;
        double ratio = value / range;
        int len = (int)(sliderLen * ratio) + 10;

        if (settingRenderUpdates) {
            setting.setVal((int)(range * ratio + settingMin));
        }

        RenderUtils.fillRoundHoriLine(context, drawX, drawY, width / 4, 10, Shades.GRAY);
        RenderUtils.fillRoundHoriLine(context, drawX, drawY, (int)(len * animator.getAnimation()), 10, Shades.GENERIC);
        RenderUtils.drawRightText(context, "" + setting.getVal(), fillEnd, drawY - 6, 0.7F, false);
        this.fillEnd = drawX + len;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            screen.selected = this;
        }
    }

    public void enableSettingRenderUpdates() {
        settingRenderUpdates = true;
    }

    public void disableSettingRenderUpdates() {
        settingRenderUpdates = false;
    }

    public IntegerSetting getSetting() {
        return setting;
    }
}
