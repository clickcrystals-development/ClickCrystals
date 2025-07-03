package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.modules.modules.misc.TotemPops;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.HealthAsBar;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TargetPositionableHud extends Hud {

    private static PlayerEntity target;
    private static long timer;
    private final Animator animator = new PollingAnimator(200, this::isTargetNaked);

    public TargetPositionableHud() {
        super("target-hud", 150, 30, 120, 16);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
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
            int margin = x + g + 2;

            // player head
            caret += g + 2;
            PlayerSkinDrawer.draw(context, targetEntry.getSkinTextures(), margin, caret, 15);

            // player name (next to player head)
            // and player ping and distance
            caret += 7;
            String name = targetEntry.getProfile().getName();
            String info = "§f" + targetEntry.getLatency() + " §7ms, §f" + MathUtils.round(target.distanceTo(p), 10) + " §7dist";
            RenderUtils.drawText(context, name, margin + 15 + g, caret, 0.8F, true);
            caret += 15;
            RenderUtils.drawText(context, info, margin, caret, 0.8F, true);

            // target armor
            caret += (int)(10 * animator.getProgressClampedReversed());
            if (!isTargetNaked()) {
                for (ItemStack item: EntityUtils.getArmorItems(target)) {
                    RenderUtils.drawItem(context, item, margin, caret - 1, 13);
                    margin += 13;
                }
                RenderUtils.drawItem(context, target.getMainHandStack(), margin, caret - 1, 13);
                RenderUtils.drawItem(context, target.getOffHandStack(), margin + 13, caret - 1, 13);
                margin = x + g + 2;
                caret += 3;
            }

            // health indicator
            // yes I was lazy, so I just used my HealthAsBar module's code
            caret += 10;
            float maxHp = target.getMaxHealth();
            float hp = target.getHealth();
            float ab = target.getAbsorptionAmount();
            HealthAsBar.render(context, margin, caret, maxHp, (int) hp, (int) hp, (int) ab, true);

            // totem indicator
            ItemStack totem = Items.TOTEM_OF_UNDYING.getDefaultStack();
            String pops = "§c-" + Module.get(TotemPops.class).getPops(target);
            float scale = 1.8F;
            int tx = (int) ((margin + 80 + g) / scale - 1);
            int ty = (int) (y / scale + 5);
            context.getMatrices().pushMatrix();
            context.getMatrices().scale(scale);
            context.drawItem(totem, tx, ty);
            context.drawStackOverlay(mc.textRenderer, totem, tx, ty, pops);
            context.getMatrices().popMatrix();

            // end
            caret += g + 8;
            setHeight(caret - y);
        }
        else {
            setHeight(12);
            String text = "Not in combat";
            int x = getX() + getWidth() / 2;
            int y = getY() + (int) (getHeight() * 0.33);
            RenderUtils.drawCenteredText(context, text, x, y, 1.0F, true);
        }
    }

    public static PlayerEntity getTarget() {
        return target;
    }

    public static void setTarget(PlayerEntity target) {
        TargetPositionableHud.target = target;
        double stay = Module.getFrom(InGameHuds.class, m -> m.hudTargetStayTime.getVal());
        timer = System.currentTimeMillis() + (long)(stay * 1000);
    }

    @Override
    public boolean canRender() {
        boolean def = super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudTarget.getVal());
        boolean autoDisable = Module.getFrom(InGameHuds.class, m -> m.hudTargetDisableWhenNoCombat.getVal());
        boolean targetValid = target != null && !target.isDead() && timer > System.currentTimeMillis();
        return def && !(!targetValid && autoDisable);
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }

    public boolean isTargetNaked() {
        if (target == null) {
            return true;
        }
        if (!target.getMainHandStack().isEmpty() || !target.getOffHandStack().isEmpty()) {
            return false;
        }
        for (ItemStack item : EntityUtils.getArmorItems(target))
            if (!item.isEmpty())
                return false;
        return true;
    }
}
