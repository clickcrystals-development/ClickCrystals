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
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.NbtUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModrinthNoNo
public class StunSlam extends ListenerModule {

    private final SettingSection sgGeneral = getGeneralSection();
    private final SettingSection sgTiming = createSettingSection("Timing");
    private final SettingSection sgConditions = createSettingSection("Conditions");
    
    private int backTimer = 0;
    private boolean awaitingBack = false;

    private final ModuleSetting<Boolean> requireDensity = sgConditions.add(BooleanSetting.create()
            .name("require-density")
            .description("Only swap if mace has density enchantment")
            .def(true)
            .build());

    private final ModuleSetting<Boolean> onlyOnShield = sgConditions.add(BooleanSetting.create()
            .name("only-on-shield")
            .description("Only swap if target is blocking with shield")
            .def(true)
            .build());

    private final ModuleSetting<Boolean> autoSwitchBack = sgGeneral.add(BooleanSetting.create()
            .name("auto-switch-back")
            .description("Automatically switch back to axe after mace hit")
            .def(true)
            .build());

    private final ModuleSetting<Integer> swapBackDelay = sgTiming.add(IntegerSetting.create()
            .name("swap-back-delay")
            .description("Delay in ticks before swapping back")
            .def(2)
            .min(0)
            .max(20)
            .build());

    public StunSlam() {
        super("stun-slam", Categories.PVP, "Switches from mace to axe when target has shield, then back to mace for devastating combos");
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        if (mc.currentScreen != null) return;
        performStunSlam();
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (awaitingBack && backTimer-- <= 0) {
            if (HotbarUtils.has(this::isAxe)) {
                HotbarUtils.search(this::isAxe);
            }
            awaitingBack = false;
        }
    }

    private void performStunSlam() {
        if (!isEnabled() || PlayerUtils.invalid() || !(mc.targetedEntity instanceof LivingEntity))
            return;
        if (!isAxe(HotbarUtils.getHand()))
            return;
        if (awaitingBack)
            return;
        if (onlyOnShield.getVal() && mc.targetedEntity instanceof PlayerEntity player && !isPlayerBlocking(player))
            return;

        ItemStack mace = findDensityMace();
        if (mace.isEmpty())
            return;
        
        system.scheduler.runDelayedTask(() -> {
            if (!HotbarUtils.search(item -> ItemStack.areEqual(item, mace)))
                return;
            
            system.scheduler.runDelayedTask(() -> {
                if (mc.targetedEntity instanceof LivingEntity) {
                    InteractionUtils.leftClick();
                }
                if (autoSwitchBack.getVal()) {
                    awaitingBack = true;
                    backTimer = swapBackDelay.getVal();
                }
            }, 1);
        }, 2);
    }

    private boolean isPlayerBlocking(PlayerEntity player) {
        return player.isBlocking() && player.getActiveItem().isOf(Items.SHIELD);
    }

    private boolean isAxe(ItemStack item) {
        return item.getItem().getTranslationKey().contains("axe");
    }

    private ItemStack findDensityMace() {
        if (requireDensity.getVal())
            return HotbarUtils.searchItem(item -> item.isOf(Items.MACE) && NbtUtils.hasEnchant(item, "density"));
        return HotbarUtils.searchItem(item -> item.isOf(Items.MACE));
    }
}
