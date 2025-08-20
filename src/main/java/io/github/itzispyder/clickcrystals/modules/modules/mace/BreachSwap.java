package io.github.itzispyder.clickcrystals.modules.modules.mace;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.EntityDamageEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class BreachSwap extends Module implements Listener {

    private final SettingSection sgGeneral = getGeneralSection();
    private final SettingSection sgTiming = createSettingSection("Timing");
    private final SettingSection sgConditions = createSettingSection("Conditions");
    
    private final BooleanSetting enabled = sgGeneral.add(BooleanSetting.create()
            .name("enabled")
            .description("Enable/disable the BreachSwap module")
            .def(true)
            .build());
            
    private final BooleanSetting requireBreach = sgConditions.add(BooleanSetting.create()
            .name("require-breach")
            .description("Only swap if mace has breach enchantment")
            .def(true)
            .build());
            
    private final BooleanSetting onDamageEvent = sgConditions.add(BooleanSetting.create()
            .name("on-damage-event")
            .description("Also trigger on damage events")
            .def(true)
            .build());
            
    private final BooleanSetting autoSwitchBack = sgGeneral.add(BooleanSetting.create()
            .name("auto-switch-back")
            .description("Automatically switch back to sword after mace hit")
            .def(true)
            .build());
    
    private final DoubleSetting switchDelay = sgTiming.add(DoubleSetting.create()
            .name("switch-delay")
            .description("Delay between weapon switches (seconds)")
            .def(0.05)
            .min(0.0)
            .max(0.1)
            .decimalPlaces(2)
            .build());
            
    private final DoubleSetting attackDelay = sgTiming.add(DoubleSetting.create()
            .name("attack-delay")
            .description("Delay between attacks (seconds)")
            .def(0.08)
            .min(0.0)
            .max(0.1)
            .decimalPlaces(2)
            .build());

    public BreachSwap() {
        super("breach-swap", Categories.PVP, "Switches from sword to breach mace when attacking entities for enhanced damage");
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
    private void onMouseClick(MouseClickEvent e) {
        if (!enabled.getVal() || e.getButton() != 0) return; // Only left click
        if (PlayerUtils.invalid()) return;
        
        performBreachSwap();
    }

    @EventHandler
    private void onAttack(PlayerAttackEntityEvent e) {
        if (!enabled.getVal()) return;
        if (PlayerUtils.invalid()) return;
        
        performBreachSwap();
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (!enabled.getVal() || !onDamageEvent.getVal()) return;
        if (PlayerUtils.invalid()) return;
        
        performBreachSwap();
    }

    private void performBreachSwap() {
        // Check if holding sword
        if (!HotbarUtils.nameContains("sword")) return;
        
        // Check if targeting entity
        if (mc.targetedEntity == null) return;
        
        // Find breach mace in hotbar
        ItemStack breachMace = findBreachMace();
        if (breachMace == null) return;
        
        // Perform the combo
        executeCombo(breachMace);
    }

    private ItemStack findBreachMace() {
        if (PlayerUtils.invalid()) return null;
        
        for (int i = 0; i <= 8; i++) {
            ItemStack stack = PlayerUtils.player().getInventory().getStack(i);
            if (stack.getItem().getTranslationKey().contains("mace")) {
                // If require breach is disabled, any mace works
                if (!requireBreach.getVal()) return stack;
                
                // Check for breach enchantment
                if (stack.hasEnchantments() && 
                    stack.getEnchantments().getEnchantments().stream()
                        .anyMatch(enchantment -> enchantment.getIdAsString().contains("breach"))) {
                    return stack;
                }
            }
        }
        return null;
    }

    private void executeCombo(ItemStack breachMace) {
        new Thread(() -> {
            try {
                // Switch to breach mace
                HotbarUtils.search(item -> item == breachMace);
                Thread.sleep((long) (switchDelay.getVal() * 1000));
                
                // Attack with breach mace
                if (mc.targetedEntity != null) {
                    mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
                Thread.sleep((long) (attackDelay.getVal() * 1000));
                
                // Switch back to sword if enabled
                if (autoSwitchBack.getVal()) {
                    HotbarUtils.search(item -> item.getItem().getTranslationKey().contains("sword"));
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
