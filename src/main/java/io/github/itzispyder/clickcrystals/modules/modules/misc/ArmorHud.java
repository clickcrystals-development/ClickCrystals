package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.InvUtils;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.function.Consumer;

public class ArmorHud extends Module {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> showMainhand = scGeneral.add(BooleanSetting.create()
            .name("show-mainhand-item")
            .description("Displays the mainhand item in the hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> showOffhand = scGeneral.add(BooleanSetting.create()
            .name("show-offhand-item")
            .description("Displays the offhand item in the hud.")
            .def(true)
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
        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();

        int x = (w / 2) + 80;
        int y = h - 21;
        for (ItemStack armorItem : p.getArmorItems()) {
            renderArmor(armorItem, context, x += 20, y);
        }

        if (showMainhand.getVal()) {
            renderHand(Hand.MAIN_HAND, context, x += 20, y);
        }
        if (showOffhand.getVal()) {
            renderHand(Hand.OFF_HAND, context, x += 20, y);
        }
    }

    private void renderArmor(ItemStack armorItem, DrawContext context, int x, int y) {
        double durability = armorItem.isDamaged() ? armorItem.getMaxDamage() - armorItem.getDamage() : armorItem.getMaxDamage();
        double ratio = durability / (double)armorItem.getMaxDamage();
        String percentage = colorPercentage(ratio * 100);

        renderItem(armorItem, context, x, y, stack -> {
            DrawableUtils.drawCenteredText(context, percentage, x + 10, y - 8, 0.8F, true);
        });
    }

    private void renderHand(Hand hand, DrawContext context, int x, int y) {
        ItemStack item = HotbarUtils.getHand(hand);
        int count = InvUtils.count(item.getItem());

        renderItem(item, context, x, y, stack -> {
            String display = count == 0 ? "" : "§b" + count;
            DrawableUtils.drawCenteredText(context, display, x + 10, y - 8, 0.8F, true);
        });
    }

    private void renderItem(ItemStack item, DrawContext context, int x, int y, Consumer<ItemStack> renderCallback) {
        context.drawTexture(GuiTextures.ITEM_WIDGET, x, y, 0, 0, 20, 20, 20, 20);
        context.drawItem(item, x + 2, y + 2);
        renderCallback.accept(item);
    }

    private String colorPercentage(double value) {
        String pre;

        if (value >= 80.0)       pre = "§a";
        else if (value >= 60.0)  pre = "§e";
        else if (value >= 40.0)  pre = "§6";
        else if (value >= 20.0)  pre = "§c";
        else                     pre = "§4";

        int i = (int)value;
        return i == 0 ? "" : pre + (int)value + "%";
    }
}
