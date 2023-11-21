package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public class ArmorHud extends Module {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Hand> hand = scGeneral.add(createEnumSetting(Hand.class)
            .name("hand-option")
            .description("Choose main hand or off hand.")
            .def(Hand.MAIN_HAND)
            .build()
    );

    public ArmorHud() {
        super("armor-hud", Categories.MISC, "Renders armor hud next to hotbar!");
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public void onRender(DrawContext context) {
        if (PlayerUtils.playerNull()) return;

        ClientPlayerEntity p = PlayerUtils.player();
        Arm arm = p.getMainArm();
        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();
        boolean right = hand.getVal() == Hand.MAIN_HAND;

        int slotW = right ? 20 : -20;
        int x = (w / 2) + (right ? 80 : -100);
        int y = h - 22;
        if (arm == Arm.RIGHT && !right) {
            x -= 30;
        }
        else if (arm == Arm.LEFT && right) {
            x += 30;
        }

        for (ItemStack armorItem : p.getArmorItems()) {
            renderItem(context, armorItem, x += slotW, y);
        }
        renderItem(context, HotbarUtils.getHand(Hand.MAIN_HAND), x += slotW, y);
        renderItem(context, HotbarUtils.getHand(Hand.OFF_HAND), x += slotW, y);
    }

    private void renderItem(DrawContext context, ItemStack item, int x, int y) {
        RenderUtils.fill(context, x, y, 22, 22, 0x90000000);
        RenderUtils.drawBorder(context, x, y, 22, 22, 1, 0xFF000000);
        RenderUtils.drawTexture(context, Tex.Defaults.ITEM_WIDGET, x, y, 22, 22);
        context.drawItem(item, x + 3, y + 3);
        context.drawItemInSlot(mc.textRenderer, item, x + 3, y + 3);
    }
}
