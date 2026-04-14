package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;

@ModrinthNoNo
public class AxeSwap extends Module implements Listener {

    private final ModuleSetting<Boolean> directionalShield = getGeneralSection().add(BooleanSetting.create()
            .name("directional-shield")
            .description("Only swap if the enemy is facing you")
            .def(true)
            .build());

    private final ModuleSetting<Double> shieldFov = getGeneralSection().add(DoubleSetting.create()
            .name("shield-fov")
            .description("Detection angle for directional shield in degrees")
            .def(60.0)
            .min(10.0)
            .max(180.0)
            .build());

    public AxeSwap() {
        super("axe-swap", Categories.PVP, "Switch to axe if hitting a shielding opponent with a sword");
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
        if (e.getEntity() instanceof Player p && EntityUtils.isBlocking(p, directionalShield.getVal(), shieldFov.getVal())) {
            if (HotbarUtils.nameContains("sword") && HotbarUtils.has(item -> item.getItem() instanceof AxeItem)) {
                HotbarUtils.search(item -> item.getItem() instanceof AxeItem);
            }
        }
    }
}
