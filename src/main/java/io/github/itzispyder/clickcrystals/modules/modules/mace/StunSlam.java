package io.github.itzispyder.clickcrystals.modules.modules.mace;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class StunSlam extends ListenerModule {

    private final SettingSection sgGeneral = getGeneralSection();
    private final SettingSection sgTiming = createSettingSection("Timing");
    
    private final BooleanSetting enabled = sgGeneral.add(BooleanSetting.create()
            .name("enabled")
            .description("Enable/disable the StunSlam module")
            .def(true)
            .build());
            
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
            
    private final DoubleSetting attackDelay = sgTiming.add(DoubleSetting.create()
            .name("attack-delay")
            .description("Delay between attacks (seconds)")
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
        if (!enabled.getVal() || e.getButton() != 0) return; // Only left click
        if (PlayerUtils.invalid()) return;
        
        performStunSlam();
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        if (!enabled.getVal()) return;
        if (PlayerUtils.invalid()) return;
        
        performStunSlam();
    }

    private void performStunSlam() {
        // Check if holding mace
        if (!HotbarUtils.nameContains("mace")) return;
        
        // Check if targeting entity
        if (mc.targetedEntity == null) return;
        
        // Check if target is a player and blocking (if setting enabled)
        if (onlyOnShield.getVal() && mc.targetedEntity instanceof PlayerEntity player) {
            if (!player.isBlocking()) return;
        }
        
        // Check if we have an axe in hotbar
        if (!HotbarUtils.has(item -> item.getItem().getTranslationKey().contains("axe"))) return;
        
        // Perform the combo
        executeCombo();
    }

    private void executeCombo() {
        long delay = switchDelay.getVal().longValue() * 1000L;
        var p = PlayerUtils.player();
        var m = PlayerUtils.getInteractions();
        
        system.scheduler.runChainTask()
                .thenRun(system.mcExecuteRunnable(() -> {
                    // Switch to axe
                    HotbarUtils.search(item -> item.getItem().getTranslationKey().contains("axe"));
                }))
                .thenWait(delay)
                .thenRun(system.mcExecuteRunnable(() -> {
                    // Attack with axe
                    if (mc.targetedEntity == null)
                        return;
                    m.attackEntity(p, mc.targetedEntity);
                    p.swingHand(Hand.MAIN_HAND);
                }))
                .thenWait(delay)
                .thenRun(system.mcExecuteRunnable(() -> {
                    // Switch back to mace if enabled
                    if (!autoSwitchBack.getVal())
                        return;
                    HotbarUtils.search(Items.MACE);
                    system.scheduler.runDelayedTask(() -> {
                        // Attack with mace
                        if (mc.targetedEntity == null)
                            return;
                        m.attackEntity(p, mc.targetedEntity);
                        p.swingHand(Hand.MAIN_HAND);
                    }, delay);
                }))
                .startChain();
    }
}
