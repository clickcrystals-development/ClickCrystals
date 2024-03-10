package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;


import static net.minecraft.item.Items.ELYTRA;

public class ElytraSwitch extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<Boolean> middleClickElytraSwitch = scGeneral.add(createBoolSetting()
            .name("middle-click-to-equip-elytra")
            .description("Equip elytra if holding the elytra and pressing middle click.")
            .def(false)
            .build()
    );
    public ElytraSwitch() {
        super("elytra-swap", Categories.PVP, "Switch to elytra if holding sword and pressing middle click.");
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
    private void onClick(MouseClickEvent e) {
        if (e.isScreenNull() && e.getAction().isDown() && e.getButton() == 2 && HotbarUtils.nameContains("sword")) {
            HotbarUtils.search(ELYTRA);
            if (middleClickElytraSwitch.getVal() && HotbarUtils.isHolding(ELYTRA)) {
                InteractionUtils.rightClick();
                        }
                    }
                }
            }