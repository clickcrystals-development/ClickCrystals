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
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

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
    public final ModuleSetting<Boolean> infiniteDurability = scGeneral.add(createBoolSetting()
            .name("infinite-elytra-durability")
            .description("replace elytra with chestplate every ~1 second to prevent durability loss, spam space bar to use efficiently")
            .def(false)
            .build()
    );

    private boolean fallFlying;
    private boolean onGlide;

    public ElytraSwitch() {
        super("elytra-switch", Categories.CRYSTAL, "Swap to elytra from your hotbar when ever you are double jumping");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        fallFlying = false;
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        fallFlying = false;
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid())
            return;

        boolean bl = PlayerUtils.player().isGliding();
        if (bl && !fallFlying && !onGlide) {
            fallFlying = true;
            onDeparture();
        } else if (!bl && fallFlying) {
            fallFlying = false;
            onTouchDown();
        }
        onGlide();
    }

    /**
     * Based on:
     * <a href="https://youtu.be/WYIMsWBxIhI?si=spZcNEqDqma9NCDo">...</a>
     * Recommended to use with fireworks for maximum distance and best results.
     **/
    public void onGlide() {
        onGlide = false;
        if (PlayerUtils.invalid() || !infiniteDurability.getVal() || !PlayerUtils.player().isGliding() || PlayerUtils.player().isSwimming())
            return;
        onGlide = true;
        if (InvUtils.isWearing(Items.ELYTRA)) {
            HotbarUtils.search(this::isChestplate);
            if (HotbarUtils.isHoldingEitherHand(this::isChestplate))
                InteractionUtils.inputUse(system.random.getRandomInt(800, 999));
        }
        onGlide = false;
    }

    public void onTouchDown() {
        if (!chestplateSwitch.getVal())
            return;

        HotbarUtils.search(this::isChestplate);
        if (HotbarUtils.isHoldingEitherHand(this::isChestplate))
            InteractionUtils.inputUse();

        if (HotbarUtils.isHoldingEitherHand(this::isChestplate))
            InteractionUtils.inputUse();
    }

    public void onDeparture() {
        if (rocketSwitch.getVal()) {
            HotbarUtils.search(this::isRocket);
            if (HotbarUtils.isHolding(Items.FIREWORK_ROCKET))
                InteractionUtils.inputUse();
        }
    }

    public boolean isRocket(ItemStack stack) {
        return stack.getItem() == Items.FIREWORK_ROCKET;
    }

    public boolean isChestplate(ItemStack stack) {
        return stack.getItem().getTranslationKey().toLowerCase().contains("chestplate");
    }
}