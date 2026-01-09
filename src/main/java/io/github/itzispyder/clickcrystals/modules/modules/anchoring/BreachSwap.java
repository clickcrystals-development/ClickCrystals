package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModrinthNoNo
public class BreachSwap extends ListenerModule {

    private final SettingSection sgGeneral = getGeneralSection();
    private final SettingSection sgTiming = createSettingSection("Timing");
    private final SettingSection sgConditions = createSettingSection("Conditions");
    
    private int backTimer = 0;
    private boolean awaitingBack = false;

    private final ModuleSetting<Boolean> requireBreach = sgConditions.add(BooleanSetting.create()
            .name("require-breach")
            .description("Only swap if mace has breach enchantment")
            .def(true)
            .build());

    private final ModuleSetting<Boolean> onArmoredOnly = sgConditions.add(BooleanSetting.create()
            .name("on-armored-only")
            .description("Only swap if target has armor")
            .def(false)
            .build());

    private final ModuleSetting<Boolean> autoSwitchBack = sgGeneral.add(BooleanSetting.create()
            .name("auto-switch-back")
            .description("Automatically switch back to sword after mace hit")
            .def(true)
            .build());

    private final ModuleSetting<Integer> swapBackDelay = sgTiming.add(IntegerSetting.create()
            .name("swap-back-delay")
            .description("Delay in ticks before swapping back")
            .def(2)
            .min(0)
            .max(20)
            .build());

    public BreachSwap() {
        super("breach-swap", Categories.PVP, "Switches from sword to breach mace when attacking entities for enhanced damage");
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        if (mc.currentScreen != null) return;
        performBreachSwap();
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (awaitingBack && backTimer-- <= 0) {
            if (HotbarUtils.has(this::isSword)) {
                HotbarUtils.search(this::isSword);
            }
            awaitingBack = false;
        }
    }

    private void performBreachSwap() {
        if (!isEnabled() || PlayerUtils.invalid() || !(mc.targetedEntity instanceof LivingEntity))
            return;
            
        if (!isSword(mc.player.getMainHandStack())) return;

        if (awaitingBack) return;

        if (onArmoredOnly.getVal() && !targetHasArmor()) return;

        var mace = findBreachMace();
        if (mace.isEmpty()) return;
        
        if (!HotbarUtils.search(item -> ItemStack.areEqual(item, mace))) return;
        
        if (autoSwitchBack.getVal()) {
            awaitingBack = true;
            backTimer = swapBackDelay.getVal();
        }
    }

    private boolean targetHasArmor() {
        if (!(mc.targetedEntity instanceof net.minecraft.entity.LivingEntity living)) return false;
        
        return !living.getEquippedStack(net.minecraft.entity.EquipmentSlot.HEAD).isEmpty() ||
               !living.getEquippedStack(net.minecraft.entity.EquipmentSlot.CHEST).isEmpty() ||
               !living.getEquippedStack(net.minecraft.entity.EquipmentSlot.LEGS).isEmpty() ||
               !living.getEquippedStack(net.minecraft.entity.EquipmentSlot.FEET).isEmpty();
    }

    private boolean isSword(ItemStack item) {
        return item.getItem().getTranslationKey().contains("sword");
    }

    private ItemStack findBreachMace() {
        if (requireBreach.getVal()) {
            return HotbarUtils.searchItem(item -> item.isOf(Items.MACE) && NbtUtils.hasEnchant(item, "breach"));
        }
        return HotbarUtils.searchItem(item -> item.isOf(Items.MACE));
    }
}
