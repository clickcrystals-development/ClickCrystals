package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.InvUtils;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ArmorHud extends Module implements Listener {

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
    public final ModuleSetting<Boolean> leftSide = scGeneral.add(BooleanSetting.create()
            .name("left-side")
            .description("Displays the hud on the left side of the hotbar.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> showEnemyInfo = scGeneral.add(BooleanSetting.create()
            .name("show-enemy-info")
            .description("Displays enemy info as a second hud above this one. §c(mostly bannable!)")
            .def(false)
            .build()
    );
    public final ModuleSetting<Double> enemyTrackingDist = scGeneral.add(DoubleSetting.create()
            .max(15)
            .min(5)
            .decimalPlaces(1)
            .name("enemy-tracking-distance")
            .description("Distance to be within of your enemy to get their information.")
            .def(10.0)
            .build()
    );
    private PlayerEntity target;
    private int tickTimer;

    public ArmorHud() {
        super("armor-hud", Categories.MISC, "Renders armor hud next to hotbar!");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        if (e.getEntity() instanceof PlayerEntity player) {
            target = player;
        }
    }

    public void onRender(DrawContext context) {
        if (PlayerUtils.playerNull()) return;

        ClientPlayerEntity p = PlayerUtils.player();
        Arm arm = p.getMainArm();
        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();
        boolean right = !leftSide.getVal();

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
            renderArmor(armorItem, context, x += slotW, y, false);
        }

        if (showMainhand.getVal()) {
            renderHand(Hand.MAIN_HAND, context, x += slotW, y, false);
        }
        if (showOffhand.getVal()) {
            renderHand(Hand.OFF_HAND, context, x += slotW, y, false);
        }

        if (showEnemyInfo.getVal() && target != null && p.getPos().distanceTo(target.getPos()) <= enemyTrackingDist.getVal()) {
            int x2 = (w / 2) + (right ? 80 : -100);
            int y2 = h - 60;

            for (ItemStack armorItem : target.getArmorItems()) {
                renderArmor(armorItem, context, x2 += slotW, y2, true);
            }

            if (showMainhand.getVal()) {
                ItemStack item = target.getStackInHand(Hand.MAIN_HAND);
                renderArmor(item, context, x2 += slotW, y2, true);
            }
            if (showOffhand.getVal()) {
                ItemStack item = target.getStackInHand(Hand.OFF_HAND);
                renderArmor(item, context, x2 += slotW, y2, true);
            }
        }
    }

    private void renderArmor(ItemStack armorItem, DrawContext context, int x, int y, boolean warning) {
        double durability = armorItem.isDamaged() ? armorItem.getMaxDamage() - armorItem.getDamage() : armorItem.getMaxDamage();
        double ratio = durability / (double)armorItem.getMaxDamage();
        String percentage = colorPercentage(ratio * 100);

        renderItem(armorItem, context, x, y, stack -> {
            if (armorItem.isDamageable()) {
                RenderUtils.drawCenteredText(context, percentage, x + 11, y - 8, 0.8F, true);
            }
        }, warning);
    }

    private void renderHand(Hand hand, DrawContext context, int x, int y, boolean warning) {
        ItemStack item = HotbarUtils.getHand(hand);
        int count = InvUtils.count(item.getItem());

        renderItem(item, context, x, y, stack -> {
            String display = count == 0 ? "" : "§b" + count;
            RenderUtils.drawCenteredText(context, display, x + 11, y - 8, 0.8F, true);
        }, warning);
    }

    private void renderItem(ItemStack item, DrawContext context, int x, int y, Consumer<ItemStack> renderCallback, boolean warning) {
        Identifier texture = warning ? GuiTextures.ITEM_WIDGET_WARNING : GuiTextures.ITEM_WIDGET;
        context.drawTexture(texture, x, y, 0, 0, 22, 22, 22, 22);
        context.drawItem(item, x + 3, y + 3);
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
