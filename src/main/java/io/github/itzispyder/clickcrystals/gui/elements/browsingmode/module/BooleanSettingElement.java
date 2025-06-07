package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class BooleanSettingElement extends SettingElement<BooleanSetting> {

    private final Animator animator = new PollingAnimator(300, this.getSetting()::getVal, Animations.ELASTIC_BOUNCE);

    public BooleanSettingElement(BooleanSetting setting, int x, int y) {
        super(setting, x, y);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        int drawY = y + height / 2;
        int drawX = x + width - 20 - 5;
        double ani = animator.getAnimation();
        boolean on = setting.getVal();

        // custom
        RenderUtils.fillRoundHoriLine(context, drawX, drawY, 20, 6, Shades.GRAY);
        RenderUtils.fillRoundHoriLine(context, drawX, drawY, (int)(20 * ani), 6, Shades.GENERIC_LOW);
        RenderUtils.fillCircle(context, drawX + 3 + (int)(14 * ani), drawY + 3, 5, on ? Shades.GENERIC : Shades.LIGHT_GRAY);
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
