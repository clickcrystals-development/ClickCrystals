package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

public class DirectionRelativeHud extends TextHud {

    public DirectionRelativeHud() {
        super("direction-hud", 10, 150, 120, 12);
    }

    @Override
    public String getText() {
        String name = "UNKNOWN";

        if (PlayerUtils.playerNotNull()) {
            name = StringUtils.capitalizeWords(PlayerUtils.player().getMovementDirection().name());
        }

        return "Facing: " + name;
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudDirection.getVal());
    }
}
