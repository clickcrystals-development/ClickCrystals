package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators.EntityIndicatorSimulation;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class EntityIndicator extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> onlyMonsters = scGeneral.add(createBoolSetting()
            .name("only-monsters")
            .description("Only render monsters on the hud.")
            .def(true)
            .build()
    );
    private final SettingSection scRender = createSettingSection("render");
    public final ModuleSetting<Boolean> updatePerRender = scRender.add(createBoolSetting()
            .name("update-per-render")
            .description("Updates every frame instead of every tick. Turning this on CAN slow down games for low end PC's.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Integer> radarRange = scRender.add(createIntSetting()
            .name("radar-range")
            .description("Radar detection range.")
            .max(64)
            .min(16)
            .def(16)
            .build()
    );
    public final ModuleSetting<Integer> hudSize = scRender.add(createIntSetting()
            .name("hud-size")
            .description("Sprite display size.")
            .max(50)
            .min(10)
            .def(50)
            .build()
    );
    public final ModuleSetting<Integer> spriteSize = scRender.add(createIntSetting()
            .name("sprite-size")
            .description("Sprite display size.")
            .max(16)
            .min(5)
            .def(10)
            .build()
    );
    public final ModuleSetting<RenderMode> renderMode = scRender.add(createEnumSetting(RenderMode.class)
            .name("render-mode")
            .description("How you want the HUD to be rendered")
            .def(RenderMode.COMPASS)
            .build()
    );

    private final EntityIndicatorSimulation simulation;

    public EntityIndicator() {
        super("entity-indicator", Categories.RENDER, "Indicates entities around you. Players are excluded");
        this.simulation = new EntityIndicatorSimulation();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        simulation.clear();
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (!updatePerRender.getVal())
            update();
    }

    public void update() {
        simulation.update(radarRange.getVal(), onlyMonsters.getVal());
    }

    public boolean isRendering2D() {
        return renderMode.getVal() == RenderMode.COMPASS;
    }

    public EntityIndicatorSimulation getSimulation() {
        return simulation;
    }

    public enum RenderMode {
        COMPASS,
        SPHERICAL
    }
}