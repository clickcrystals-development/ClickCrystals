package io.github.itzispyder.clickcrystals.gui_beta.hud.moveables;

import io.github.itzispyder.clickcrystals.gui_beta.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;

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
