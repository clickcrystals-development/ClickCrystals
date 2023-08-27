package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.gui.hud.moveables.TargetRelativeHud;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.entity.player.PlayerEntity;

public class InGameHuds extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scHudTarget = createSettingSection("target-hud-settings");
    public final ModuleSetting<Boolean> hudIcon = scGeneral.add(BooleanSetting.create()
            .name("render-icon-hud")
            .description("Renders the icon hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudPing = scGeneral.add(BooleanSetting.create()
            .name("render-ping-hud")
            .description("Renders the ping hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudFps = scGeneral.add(BooleanSetting.create()
            .name("render-fps-hud")
            .description("Renders the fps hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudClock = scGeneral.add(BooleanSetting.create()
            .name("render-clock-hud")
            .description("Renders the clock hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudTarget = scGeneral.add(BooleanSetting.create()
            .name("render-target-hud")
            .description("Renders the target hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudPos = scGeneral.add(BooleanSetting.create()
            .name("render-position-hud")
            .description("Renders the position hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Double> hudTargetStayTime = scHudTarget.add(DoubleSetting.create()
            .min(5.0)
            .max(120.0)
            .decimalPlaces(1)
            .name("target-hud-stay-time")
            .description("Tracking time of current target.")
            .def(10.0)
            .build()
    );

    public InGameHuds() {
        super("in-game-huds", Categories.CLICKCRYSTALS,"Manager of all custom ClickCrystals in-game huds.");
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
        if (e.getEntity() instanceof PlayerEntity player && hudTarget.getVal()) {
            TargetRelativeHud.setTarget(player);
        }
    }
}
