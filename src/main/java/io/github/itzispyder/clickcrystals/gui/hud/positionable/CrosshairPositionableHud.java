package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class CrosshairPositionableHud extends Hud {

    public CrosshairPositionableHud() {
        super("crosshair-hud", 10, 165, 120, 16);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        renderBackdrop(context);

        if (mc.crosshairTarget instanceof EntityHitResult hit) {
            String name = "Target: " + StringUtils.capitalizeWords(getKeyOf(hit.getEntity().getType().getTranslationKey()));
            setWidth(mc.textRenderer.getWidth(name) + 6);

            int x = getX() + getWidth() / 2;
            int y = getY() + (int)(getHeight() * 0.33);
            RenderUtils.drawCenteredText(context, name, x, y, 1.0F, true);
        }
        else if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.MISS) {
            String name = "Target: §8Air";
            setWidth(mc.textRenderer.getWidth(name) + 6);

            int x = getX() + getWidth() / 2;
            int y = getY() + (int)(getHeight() * 0.33);
            RenderUtils.drawCenteredText(context, name, x, y, 1.0F, true);
        }
        else if (mc.crosshairTarget instanceof BlockHitResult hit) {
            setWidth(60);
            BlockState state = PlayerUtils.player().getWorld().getBlockState(hit.getBlockPos());
            int x = getX() + 3;
            int y = getY() + (int)(getHeight() * 0.33);
            RenderUtils.drawText(context, "Target: ", x, y, 1.0F, true);

            float scale = 0.8F;
            x = (int)((getX() + getWidth() * 0.75) / scale);
            y = (int)(getY() / scale);
            context.getMatrices().pushMatrix();
            context.getMatrices().scale(scale);
            context.drawItem(state.getBlock().asItem().getDefaultStack(), x, y);
            context.getMatrices().popMatrix();
        }
    }

    private String getKeyOf(String translationKey) {
        String[] key = translationKey.split("\\.");
        return key[key.length - 1];
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudCrosshair.getVal());
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }
}
