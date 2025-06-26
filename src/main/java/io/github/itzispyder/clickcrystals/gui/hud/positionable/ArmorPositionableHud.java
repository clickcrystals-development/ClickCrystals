package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class ArmorPositionableHud extends Hud {

    public ArmorPositionableHud() {
        super("armor-hud", 387, 222, 80, 20);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (PlayerUtils.player() != null) {
            renderBackdrop(context);

            int x = getX();
            int y = getY();

            onRender(context, x, y);
        }
    }

    private void onRender(DrawContext context, int x, int y) {
        if (PlayerUtils.invalid()) return;

        ClientPlayerEntity player = PlayerUtils.player();
        int slotHeight = 20;
        int slotWidth = 20;
        int armorCount = 4;
        int totalWidth = slotWidth * armorCount;

        int startX = x + (getWidth() - totalWidth) / 2 + 1;
        int startY = y + (getHeight() - slotHeight) / 2 + 2;

        for (ItemStack armorItem : EntityUtils.getArmorItems(player)) {
            RenderUtils.drawItem(context, armorItem, startX, startY);
            startX += slotWidth;
        }
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudArmor.getVal());
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, InGameHuds::getArgb);
    }
}
