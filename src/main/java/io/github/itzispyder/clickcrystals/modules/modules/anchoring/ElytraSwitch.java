package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import net.minecraft.item.ItemStack;

public class ElytraSwitch extends DummyModule implements Listener, Global {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> chestplateSwitch = scGeneral.add(createBoolSetting()
            .name("chestplate-switch")
            .description("switch to chestplate after landing on the ground")
            .def(false)
            .build()
    );

    public final ModuleSetting<Boolean> rocketSwitch = scGeneral.add(createBoolSetting()
            .name("rocket-switch")
            .description("switch to rockets and use one after switching to elytra")
            .def(false)
            .build()
    );

    private boolean fallFlying;

    public ElytraSwitch() {
        super("Elytra Switch", Categories.CRYSTAL, "Swap to elytra from your hotbar whenever you are double jumping");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        reset();
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        reset();
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        boolean bl = mc.player.isFallFlying();
        if (bl && !fallFlying) {
            fallFlying = true;
            onDeparture();
        } else if (!bl && fallFlying) {
            fallFlying = false;
            onTouchDown();
        }
    }

    public void onTouchDown() {
        if (chestplateSwitch.getVal()) {
            HotbarUtils.search(this::isChestplate);
            InteractionUtils.inputUse();
        }
    }

    public void onDeparture() {
        if (rocketSwitch.getVal()) {
            HotbarUtils.search(this::isRocket);
            InteractionUtils.inputUse();
        }
    }

    public boolean isRocket(ItemStack stack) {
        return stack.getItem().getTranslationKey().toLowerCase().contains("rocket");
    }

    public void reset() {
        fallFlying = false;
    }

    public boolean isChestplate(ItemStack stack) {
        return stack.getItem().getTranslationKey().toLowerCase().contains("chestplate");
    }
}
