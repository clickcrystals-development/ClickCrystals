package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

public class PosRelativeHud extends TextHud {

    public PosRelativeHud() {
        super("pos-hud", 10, 120, 120, 12);
    }

    @Override
    public String getText() {
        return PlayerUtils.player().getBlockPos().toShortString();
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudPos.getVal());
    }
}
