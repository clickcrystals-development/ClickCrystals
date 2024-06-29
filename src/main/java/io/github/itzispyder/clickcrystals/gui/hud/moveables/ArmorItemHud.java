package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class ArmorItemHud extends Hud {

    public ArmorItemHud() {
        super("armor-hud", 387, 222, 80 , 20);
    }

    @Override
    public void render(DrawContext context) {
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
        int slotWidth = 20;

        for (ItemStack armorItem : player.getArmorItems()) {
            renderItem(context, armorItem, x, y);
            x += slotWidth;
        }
    }

    private void renderItem(DrawContext context, ItemStack item, int x, int y) {
        context.drawItem(item, x, y);
        context.drawItemInSlot(mc.textRenderer, item, x, y);
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
