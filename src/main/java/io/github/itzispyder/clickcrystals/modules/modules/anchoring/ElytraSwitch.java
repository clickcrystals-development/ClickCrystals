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


@SuppressWarnings("DataFlowIssue")
public class ElytraSwitch extends DummyModule implements Listener, Global {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> chestplateSwitch = scGeneral.add(createBoolSetting()
            .name("chestplate-switch")
            .description("switch to chestplate after landing on the ground")
            .def(true)
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
    }

    public void reset() {
        fallFlying = false;
    }

    public boolean isChestplate(ItemStack stack) {
        return stack.getItem().getTranslationKey().toLowerCase().contains("chestplate");
    }
}
/*TODO:
 Optional! - Make Auto Switch to rockets and then use one rocket(Maybe it will trigger the ac so it's not the best idea) after the elytra swapped
 */