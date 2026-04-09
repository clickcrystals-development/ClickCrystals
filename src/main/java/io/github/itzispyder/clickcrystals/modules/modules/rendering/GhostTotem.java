package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class GhostTotem extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Mode> renderMode = scGeneral.add(EnumSetting.create(Mode.class)
            .name("render-mode")
            .description("Ghost totem render mode.")
            .def(Mode.OffHand)
            .build()
    );

    public GhostTotem() {
        super("ghost-totem", Categories.RENDER, "Renders a totem in your hand upon dying. Will not work if AutoRespawn is enabled!");
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
    private void onDeathScreen(SetScreenEvent e) {
        if (e.getScreen() instanceof DeathScreen) {
            switch (renderMode.getVal()) {
                case Both -> {
                    totem(InteractionHand.MAIN_HAND);
                    totem(InteractionHand.OFF_HAND);
                }
                case MainHand -> {
                    totem(InteractionHand.MAIN_HAND);
                }
                default -> {
                    totem(InteractionHand.OFF_HAND);
                }
            }
        }
    }

    private void totem(InteractionHand hand) {
        PlayerUtils.player().setItemInHand(hand, Items.TOTEM_OF_UNDYING.getDefaultInstance());
    }

    public enum Mode {
        MainHand,
        OffHand,
        Both
    }
}
