package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

public class FpsPositionableHud extends TextHud {

    public FpsPositionableHud() {
        super("fps-hud", 10, 90, 50, 16);
    }

    @Override
    public String getText() {
        return PlayerUtils.getFps() + " fps";
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudFps.getVal());
    }
}
