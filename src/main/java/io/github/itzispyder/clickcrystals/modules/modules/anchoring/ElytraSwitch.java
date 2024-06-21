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
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

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

        boolean bl = PlayerUtils.player().isFallFlying();
        if (bl && !fallFlying) {
            fallFlying = true;
            onDeparture();
        }
        else if (!bl && fallFlying) {
            fallFlying = false;
            onTouchDown();
        }
    }
    public void onTouchDown() {
        if (!chestplateSwitch.getVal())
            return;

        HotbarUtils.search(this::isChestplate);
        use();

        if (HotbarUtils.isHoldingEitherHand(this::isChestplate))
            use();
    }

    public void onDeparture() {
        if (rocketSwitch.getVal()) {
            HotbarUtils.search(this::isRocket);
            HotbarUtils.search(Items.FIREWORK_ROCKET);
            HotbarUtils.isHolding(Items.FIREWORK_ROCKET);
            use();
        }
    }

    public boolean isRocket(ItemStack stack) {
        return stack.getItem() == Items.FIREWORK_ROCKET;
    }

    public boolean isChestplate(ItemStack stack) {
        return stack.getItem().getTranslationKey().toLowerCase().contains("chestplate");
    }

    private void use() {
        PlayerUtils.getInteractions().interactItem(PlayerUtils.player(), Hand.MAIN_HAND);
    }
}
