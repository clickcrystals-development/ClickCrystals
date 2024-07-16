package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Collection;

public class PotionRelativeHud extends Hud {

    public PotionRelativeHud() {
        super("potion-hud", 387, 120, 95, 90);
    }

    @Override
    public void render(DrawContext context) {
        if (PlayerUtils.player() != null) {
            renderBackdrop(context);

            int x = getX();
            int y = getY();

            RenderUtils.drawText(context, "Potions:", x, y, true);

            onRender(context, x, y + 20);
        }
    }

    private void onRender(DrawContext context, int x, int y) {
        if (PlayerUtils.invalid()) return;

        ClientPlayerEntity p = PlayerUtils.player();
        Collection<StatusEffectInstance> effects = p.getStatusEffects();

        int offsetY = 0;
        for (StatusEffectInstance effect : effects) {
            StatusEffect effectType =effect.getEffectType().value();
            String effectName = effectType.getName().getString();
            String effectDuration = formatDuration(effect.getDuration());
            String effectText = effectName + " (" + effectDuration + ")";

            RenderUtils.drawText(context, effectText, x, y + offsetY, 0.5F, true);
            offsetY += 10;
        }
    }

    private String formatDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudPotion.getVal() && PlayerUtils.isEffected());
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, InGameHuds::getArgb);
    }
}
