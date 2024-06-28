package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ArmorHud;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class ArmorItemHud extends Hud {
    ArmorHud armorHud = Module.get(ArmorHud.class);

    public ArmorItemHud() {
        super("armor-hud", 340, 228, 80 , 20);
    }

    @Override
    public void render(DrawContext context) {
        if (armorHud.isEnabled() && PlayerUtils.player() != null) {
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
        return super.canRender() && armorHud.isEnabled();
    }

    @Override
    public int getArgb() {
        return armorHud.getArgb();
    }
}
