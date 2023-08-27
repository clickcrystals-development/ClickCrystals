package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.hud.moveables.TargetRelativeHud;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ColorHelper;

public class InGameHuds extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scHudVisibility = createSettingSection("hud-visibility");
    private final SettingSection scHudTarget = createSettingSection("target-hud-settings");
    public final ModuleSetting<Boolean> renderHudBorders = scGeneral.add(BooleanSetting.create()
            .name("render-hud-borders")
            .description("Renders a border around huds.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Double> colorRed = scGeneral.add(DoubleSetting.create()
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .name("color-red")
            .description("Hud backdrop color value (red)")
            .def((double)ColorHelper.Argb.getRed(Hud.DEFAULT_ARGB))
            .build()
    );
    public final ModuleSetting<Double> colorGreen = scGeneral.add(DoubleSetting.create()
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .name("color-green")
            .description("Hud backdrop color value (green)")
            .def((double)ColorHelper.Argb.getGreen(Hud.DEFAULT_ARGB))
            .build()
    );
    public final ModuleSetting<Double> colorBlue = scGeneral.add(DoubleSetting.create()
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .name("color-blue")
            .description("Hud backdrop color value (blue)")
            .def((double)ColorHelper.Argb.getBlue(Hud.DEFAULT_ARGB))
            .build()
    );
    public final ModuleSetting<Boolean> hudIcon = scHudVisibility.add(BooleanSetting.create()
            .name("render-icon-hud")
            .description("Renders the icon hud.")
            .def(true)
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

    public int getArgb() {
        int a = ColorHelper.Argb.getAlpha(Hud.DEFAULT_ARGB);
        double r = colorRed.getVal();
        double g = colorGreen.getVal();
        double b = colorBlue.getVal();
        return ColorHelper.Argb.getArgb(a, (int)r, (int)g, (int)b);
    }
}
