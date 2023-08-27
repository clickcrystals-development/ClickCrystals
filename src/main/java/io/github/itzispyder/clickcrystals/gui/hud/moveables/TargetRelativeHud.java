package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.modules.modules.misc.TotemPops;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.HealthAsBar;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class TargetRelativeHud extends Hud {

    private static PlayerEntity target;
    private static long timer;

    public TargetRelativeHud() {
        super("target-hud", 150, 30, 120, 120);
    }

    @Override
    public void render(DrawContext context) {
        renderBackdrop(context);

        if (target != null && !target.isDead() && timer > System.currentTimeMillis()) {
            ClientPlayerEntity p = PlayerUtils.player();
            PlayerListEntry targetEntry = p.networkHandler.getPlayerListEntry(target.getGameProfile().getId());

            if (targetEntry == null) {
                target = null;
                return;
            }

            int g = 2;
            int x = getX();
            int y = getY();
            int caret = y;

            // player head
            caret += g;
            PlayerSkinDrawer.draw(context, targetEntry.getSkinTexture(), x + g, caret, 15, true, false);

            // player name (next to player head)
            caret += 5;
            String text = targetEntry.getProfile().getName();
            RenderUtils.drawText(context, text, x + g + 15 + g, caret, 0.8F, true);

            // health indicator
            // yes I was lazy, so I just used my HealthAsBar module's code
            caret += 15;
            float maxHp = target.getMaxHealth();
            float hp = target.getHealth();
            float ab = target.getAbsorptionAmount();
            Module.get(HealthAsBar.class).renderHealthBar(context, x + g, caret, maxHp, (int)hp, (int)hp, (int)ab);

            // totem indicator
            context.getMatrices().push();
            float scale = 2.0F;
            int tx = (int)((x + g + 80 + g) / scale);
            int ty = (int)(y / scale);
            context.getMatrices().scale(scale, scale, scale);
            context.drawItem(Items.TOTEM_OF_UNDYING.getDefaultStack(), tx, ty);
            context.getMatrices().pop();

            // totem indicator text
            String pops = "§c-" + Module.get(TotemPops.class).getPops(target);
            RenderUtils.drawRightText(context, pops, x + g + 80 + 32 + g, y + g + 16, 1.0F, true);

            caret += g + 8;
            setHeight(caret - y);
        }
        else {
            setHeight(12);
            String text = "Not in combat";
            int x = getX() + getWidth() / 2;
            int y = getY() + (int)(getHeight() * 0.33);
            RenderUtils.drawCenteredText(context, text, x, y, 1.0F, true);
        }
    }

    public static PlayerEntity getTarget() {
        return target;
    }

    public static void setTarget(PlayerEntity target) {
        TargetRelativeHud.target = target;
        double stay = Module.getFrom(InGameHuds.class, m -> m.hudTargetStayTime.getVal());
        timer = System.currentTimeMillis() + (long)(stay * 1000);
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudTarget.getVal());
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }

    @Override
    public boolean canRenderBorder() {
        return Module.getFrom(InGameHuds.class, m -> m.renderHudBorders.getVal());
    }
}
