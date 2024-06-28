package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.util.math.ColorHelper;

public class ArmorHud extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
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

    public int getArgb() {
        int a = colorAlpha.getVal();
        int r = colorRed.getVal();
        int g = colorGreen.getVal();
        int b = colorBlue.getVal();
        return ColorHelper.Argb.getArgb(a, r, g, b);
    }

    public ArmorHud() {
        super("armor-hud", Categories.MISC, "Renders armor hud next to hotbar!");
    }
}
