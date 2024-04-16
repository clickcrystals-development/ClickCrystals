package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class TotemOverlay extends Module {

    public final SettingSection general = getGeneralSection();

    public final ModuleSetting<Boolean> fill = general.add(BooleanSetting.create()
            .name("Fill")
            .description("Renders the transparent red fill")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> border = general.add(BooleanSetting.create()
            .name("Border")
            .description("Renders the solid red border")
            .def(false)
            .build()
    );

    public final ModuleSetting<Boolean> icon = general.add(BooleanSetting.create()
            .name("Crosshair icon")
            .description("A Small Icon by your crosshair indicating that you have no totem.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> meme = general.add(BooleanSetting.create()
            .name("Meme")
            .description("Shows a ',:| No Totem? Meme at the top of your screen")
            .def(false)
            .build()
    );

    public TotemOverlay() {
        super("totem-overlay", Categories.RENDER, "Renders red overlay when not holding totem");
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}