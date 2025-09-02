package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.EntityDamageEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BreachSwap extends ListenerModule {

    private final SettingSection sgGeneral = getGeneralSection();
    private final SettingSection sgTiming = createSettingSection("Timing");
    private final SettingSection sgConditions = createSettingSection("Conditions");

    private final ModuleSetting<Boolean> requireBreach = sgConditions.add(BooleanSetting.create()
            .name("require-breach")
            .description("Only swap if mace has breach enchantment")
            .def(true)
            .build());

    private final ModuleSetting<Boolean> onDamageEvent = sgConditions.add(BooleanSetting.create()
            .name("on-damage-event")
            .description("Also trigger on damage events")
            .def(true)
            .build());

    private final ModuleSetting<Boolean> autoSwitchBack = sgGeneral.add(BooleanSetting.create()
            .name("auto-switch-back")
            .description("Automatically switch back to sword after mace hit")
            .def(true)
            .build());

    private final ModuleSetting<Double> switchDelay = sgTiming.add(DoubleSetting.create()
            .name("switch-delay")
            .description("Delay between weapon switches (seconds)")
            .def(0.05)
            .min(0.0)
            .max(0.1)
            .decimalPlaces(2)
            .build());

    public BreachSwap() {
        super("breach-swap", Categories.PVP, "Switches from sword to breach mace when attacking entities for enhanced damage");
    }

    @EventHandler
    private void onMouseClick(MouseClickEvent e) {
        if (e.getButton() == 0)
            performBreachSwap();
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        performBreachSwap();
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (onDamageEvent.getVal())
            performBreachSwap();
    }

    private void performBreachSwap() {
        if (!isEnabled() && PlayerUtils.invalid() && !HotbarUtils.has(this::isSword) && !EntityUtils.isTargetValid())
            return;

        var mace = findBreachMace();
        if (mace == null) return;
        executeCombo(mace);
    }

    private void executeCombo(ItemStack breachMace) {
        long delay = switchDelay.getVal().longValue() * 1000L;
        system.scheduler.runChainTask()
                .thenRun(system.mcExecuteRunnable(() -> HotbarUtils.search(item -> item.isOf(breachMace.getItem()))))
                .thenWait(delay)
                .thenRun(system.mcExecuteRunnable(InteractionUtils::inputAttack))
                .thenWait(delay)
                .thenRun(system.mcExecuteRunnable(() -> {
                    if (autoSwitchBack.getVal()) HotbarUtils.search(this::isSword);
                }))
                .startChain();
    }

    private boolean isSword(ItemStack item) {
        return item.getItem().getTranslationKey().contains("sword");
    }

    private ItemStack findBreachMace() {
        return HotbarUtils.searchItem(item -> item.isOf(Items.MACE) && (NbtUtils.hasEnchant(item, "breach") || !requireBreach.getVal()));
    }
}
