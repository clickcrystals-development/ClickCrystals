package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class PearlCustomizer extends DummyModule {

    public PearlCustomizer() {
        super("pearl-customizer", Categories.RENDER, "Customize appearance and sound settings for ender pearls");
    }

    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<Integer> pearlSize = scGeneral.add(createIntSetting()
            .name("pearl-size")
            .description("Size of the ender pearl.")
            .def(1)
            .max(10)
            .min(0)
            .build()
    );

    public final ModuleSetting<Boolean> pearlSound = scGeneral.add(createBoolSetting()
            .name("pearl-sound")
            .description("Play sound on throwing ender pearls")
            .def(false)
            .build()
    );
}
