package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.trajectories.ProjectilePath;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Trajectories extends ListenerModule {

    public static final ProjectilePath BOW_SIM = new ProjectilePath(0.99, 0.6, -0.05, 3.0);
    public static final ProjectilePath CROSSBOW_SIM = new ProjectilePath(0.99, 0.6, -0.05, 3.15);
    public static final ProjectilePath TRIDENT_SIM = new ProjectilePath(0.99, 0.6, -0.05, 2.5);
    public static final ProjectilePath THROW_SIM = new ProjectilePath(0.99, 0.6, -0.03, 1.5);
    public static final ProjectilePath POTION_SIM = new ProjectilePath(0.99, 0.6, -0.05, 0.5);
    public static final ProjectilePath EXP_SIM = new ProjectilePath(0.99, 0.6, -0.07, 0.7);

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> trackBows = scGeneral.add(createBoolSetting()
            .name("bows")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> trackCrossbows = scGeneral.add(createBoolSetting()
            .name("crossbows")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> trackTridents = scGeneral.add(createBoolSetting()
            .name("tridents")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> trackPearls = scGeneral.add(createBoolSetting()
            .name("ender-pearls")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> trackSnowballs = scGeneral.add(createBoolSetting()
            .name("snowballs")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> trackEggs = scGeneral.add(createBoolSetting()
            .name("eggs")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> trackPotions = scGeneral.add(createBoolSetting()
            .name("potions")
            .def(true)
            .build()
    );
    private final SettingSection scRender = createSettingSection("render");
    public final ModuleSetting<Double> renderOffset = scRender.add(createDoubleSetting()
            .name("render-offset")
            .description("Shifts your trajectory render to the left or right")
            .decimalPlaces(2)
            .max(0.1)
            .min(-0.1)
            .def(0.0)
            .build()
    );


    private ProjectilePath.Result currentResult;
    private float tickDelta;

    public Trajectories() {
        super("trajectories", Categories.RENDER, "Simulates arrow trajectories and draws a line!");
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid())
            return;

        ClientPlayerEntity p = PlayerUtils.player();
        ItemStack active = p.getActiveItem();
        boolean using = p.isUsingItem();
        int useTime = p.getItemUseTime();
        double renderOffset = this.renderOffset.getVal();

        if (trackBows.getVal() && using && active.isOf(Items.BOW)) {
            currentResult = BOW_SIM.simulate(100, BowItem.getPullProgress(useTime), tickDelta, renderOffset);
            return;
        }
        if (trackCrossbows.getVal() && HotbarUtils.isHolding(Items.CROSSBOW) && CrossbowItem.isCharged(HotbarUtils.getHand())) {
            currentResult = CROSSBOW_SIM.simulate(100, 1.0F, tickDelta, renderOffset);
            return;
        }
        if (trackTridents.getVal() && using && active.isOf(Items.TRIDENT)) {
            currentResult = TRIDENT_SIM.simulate(100, 1.0F, tickDelta, renderOffset);
            return;
        }
        if (trackPearls.getVal() && HotbarUtils.isHolding(Items.ENDER_PEARL)) {
            currentResult = THROW_SIM.simulate(100, 1.0F, tickDelta, renderOffset);
            return;
        }
        if (trackSnowballs.getVal() && HotbarUtils.isHolding(Items.SNOWBALL)) {
            currentResult = THROW_SIM.simulate(100, 1.0F, tickDelta, renderOffset);
            return;
        }
        if (trackEggs.getVal() && HotbarUtils.isHolding(Items.EGG)) {
            currentResult = THROW_SIM.simulate(100, 1.0F, tickDelta, renderOffset);
            return;
        }
        if (trackPotions.getVal()) {
            if (HotbarUtils.isHolding(Items.SPLASH_POTION) || HotbarUtils.isHolding(Items.LINGERING_POTION)) {
                currentResult = POTION_SIM.simulate(100, 1.0F, tickDelta, renderOffset);
                return;
            }
            if (HotbarUtils.isHolding(Items.EXPERIENCE_BOTTLE)) {
                currentResult = EXP_SIM.simulate(100, 1.0F, tickDelta, renderOffset);
                return;
            }
        }
        currentResult = null;
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent e) {
        tickDelta = e.getTickCounter().getTickProgress(true);
        if (currentResult != null)
            currentResult.draw(e, tickDelta);
    }

    public float getTickDelta() {
        return tickDelta;
    }
}