package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

public class PingRelativeHud extends TextHud {

    public PingRelativeHud() {
        super("ping-hud", 10, 75, 50, 12);
    }

    @Override
    public String getText() {
        return PlayerUtils.getPing() + " ms";
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudPing.getVal());
    }
}
