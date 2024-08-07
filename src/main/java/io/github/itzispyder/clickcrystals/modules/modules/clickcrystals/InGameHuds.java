package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.hud.moveables.TargetRelativeHud;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ColorHelper;

public class InGameHuds extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scHudVisibility = createSettingSection("hud-visibility");
    private final SettingSection scHudTarget = createSettingSection("target-hud-settings");
    private final SettingSection scHudClock = createSettingSection("clock-hud-settings");
    public final ModuleSetting<Integer> colorRed = scGeneral.add(IntegerSetting.create()
            .max(255)
            .min(0)
            .name("color-red")
            .description("Hud backdrop color value (red)")
            .def(ColorHelper.Argb.getRed(Hud.DEFAULT_ARGB))
            .build()
    );
    public final ModuleSetting<Integer> colorGreen = scGeneral.add(IntegerSetting.create()
            .max(255)
            .min(0)
            .name("color-green")
            .description("Hud backdrop color value (green)")
            .def(ColorHelper.Argb.getGreen(Hud.DEFAULT_ARGB))
            .build()
    );
    public final ModuleSetting<Integer> colorBlue = scGeneral.add(IntegerSetting.create()
            .max(255)
            .min(0)
            .name("color-blue")
            .description("Hud backdrop color value (blue)")
            .def(ColorHelper.Argb.getBlue(Hud.DEFAULT_ARGB))
            .build()
    );
    public final ModuleSetting<Integer> colorAlpha = scGeneral.add(IntegerSetting.create()
            .max(255)
            .min(0)
            .name("color-alpha")
            .description("Hud backdrop color value (alpha or transparency)")
            .def(ColorHelper.Argb.getAlpha(Hud.DEFAULT_ARGB))
            .build()
    );
    public final ModuleSetting<Boolean> hudArmor = scHudVisibility.add(BooleanSetting.create()
            .name("render-armor-hud")
            .description("Renders the armor hud.")
            .def(true)
            .build()
            );
    public final ModuleSetting<Boolean> hudIcon = scHudVisibility.add(BooleanSetting.create()
            .name("render-icon-hud")
            .description("Renders the icon hud.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> hudPing = scHudVisibility.add(BooleanSetting.create()
            .name("render-ping-hud")
            .description("Renders the ping hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudFps = scHudVisibility.add(BooleanSetting.create()
            .name("render-fps-hud")
            .description("Renders the fps hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudCps = scHudVisibility.add(BooleanSetting.create()
            .name("render-cps-hud")
            .description("Renders the clicks per second hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudClock = scHudVisibility.add(BooleanSetting.create()
            .name("render-clock-hud")
            .description("Renders the clock hud.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> hudTarget = scHudVisibility.add(BooleanSetting.create()
            .name("render-target-hud")
            .description("Renders the target hud.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> hudPos = scHudVisibility.add(BooleanSetting.create()
            .name("render-position-hud")
            .description("Renders the position hud.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> hudBiome = scHudVisibility.add(BooleanSetting.create()
            .name("render-biome-hud")
            .description("Renders the biome hud.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> hudDirection = scHudVisibility.add(BooleanSetting.create()
            .name("render-direction-hud")
            .description("Renders the direction hud.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> hudCrosshair = scHudVisibility.add(BooleanSetting.create()
            .name("render-crosshair-hud")
            .description("Renders the crosshair target hud.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> hudRotation = scHudVisibility.add(BooleanSetting.create()
            .name("render-rotation-hud")
            .description("Renders the rotation hud.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> hudResource = scHudVisibility.add(BooleanSetting.create()
            .name("render-resource-hud")
            .description("Renders the resource hud.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Double> hudTargetStayTime = scHudTarget.add(DoubleSetting.create()
            .min(5.0)
            .max(600.0) // 10 minutes (600 seconds)
            .decimalPlaces(1)
            .name("target-hud-stay-time")
            .description("Tracking time of current target.")
            .def(10.0)
            .build()
    );
    public final ModuleSetting<Boolean> hudTargetDisableWhenNoCombat = scHudTarget.add(createBoolSetting()
            .name("target-hud-no-combat-disable")
            .description("Disables rendering of the target hud when not in combat.")
            .def(false)
            .build()
    );
    public final ModuleSetting<ClockDisplay> hudClockHourDisplay = scHudClock.add(EnumSetting.create(ClockDisplay.class)
            .name("clock-hud-hour-display")
            .description("Clock hour display.")
            .def(ClockDisplay.HOUR_12)
            .build()
    );

    public InGameHuds() {
        super("in-game-huds", Categories.CLIENT,"Manager of all custom ClickCrystals in-game huds");
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

    public int getArgb() {
        int a = colorAlpha.getVal();
        int r = colorRed.getVal();
        int g = colorGreen.getVal();
        int b = colorBlue.getVal();
        return ColorHelper.Argb.getArgb(a, r, g, b);
    }

    public enum ClockDisplay {
        HOUR_12,
        HOUR_24
    }
}
