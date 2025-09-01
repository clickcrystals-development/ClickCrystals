package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class StunSlam extends ListenerModule {

    private final SettingSection sgGeneral = getGeneralSection();
    private final SettingSection sgTiming = createSettingSection("Timing");

    private final BooleanSetting onlyOnShield = sgGeneral.add(BooleanSetting.create()
            .name("only-on-shield")
            .description("Only activate when target is blocking with shield")
            .def(true)
            .build());

    private final BooleanSetting autoSwitchBack = sgGeneral.add(BooleanSetting.create()
            .name("auto-switch-back")
            .description("Automatically switch back to mace after axe hit")
            .def(true)
            .build());

    private final DoubleSetting switchDelay = sgTiming.add(DoubleSetting.create()
            .name("switch-delay")
            .description("Delay between weapon switches (seconds)")
            .def(0.02)
            .min(0.0)
            .max(0.1)
            .decimalPlaces(2)
            .build());

    public StunSlam() {
        super("stun-slam", Categories.PVP, "Switches from mace to axe when target has shield, then back to mace for devastating combos");
    }

    @EventHandler
    private void onMouseClick(MouseClickEvent e) {
        if (PlayerUtils.invalid() && !isEnabled() || e.getButton() != 0)
            return;

        performStunSlam();
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        if (!isEnabled() && PlayerUtils.invalid())
            return;

        performStunSlam();
    }

    private void performStunSlam() {
        if (!HotbarUtils.nameContains("mace") || !EntityUtils.isTargetValid())
            return;

        if (onlyOnShield.getVal() && mc.targetedEntity instanceof PlayerEntity player && !player.isBlocking())
            return;

        if (!HotbarUtils.has(this::isAxe))
            return;

        executeCombo();
    }

    private void executeCombo() {
        long delay = switchDelay.getVal().longValue() * 1000L;
        system.scheduler.runChainTask()
                .thenRun(system.mcExecuteRunnable(() -> HotbarUtils.search(this::isAxe)))
                .thenWait(delay)
                .thenRun(system.mcExecuteRunnable(this::attack))
                .thenWait(delay)
                .thenRun(system.mcExecuteRunnable(() -> {
                    if (!autoSwitchBack.getVal())
                        return;
                    HotbarUtils.search(Items.MACE);
                    system.scheduler.runDelayedTask(this::attack, delay);
                }))
                .startChain();
    }

    private boolean isAxe(ItemStack item) {
        return item.getItem().getTranslationKey().contains("axe");
    }

    private void attack() {
        if (!EntityUtils.isTargetValid()) return;
        InteractionUtils.inputAttack();
    }
}
