package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class TotemPopColor extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Integer> red = scGeneral.add(createIntSetting()
            .name("Red")
            .description("Decide how much red will be on the color pattern.")
            .def(0xFF)
            .max(0xFF)
            .min(0)
            .build()
    );
    public final ModuleSetting<Integer> green = scGeneral.add(createIntSetting()
            .name("Green")
            .description("Decide how much green will be on the color pattern.")
            .def(0xFF)
            .max(0xFF)
            .min(0)
            .build()
    );
    public final ModuleSetting<Integer> blue = scGeneral.add(createIntSetting()
            .name("Blue")
            .description("Decide how much blue will be on the color pattern.")
            .def(0xFF)
            .max(0xFF)
            .min(0)
            .build()
    );
    public final ModuleSetting<Integer> alpha = scGeneral.add(createIntSetting()
            .name("Alpha")
            .description("Set transparency of the color.")
            .def(0xFF)
            .max(0xFF)
            .min(0)
            .build()
    );

    public TotemPopColor(){
        super("totem-color", Categories.RENDER, "Change totem pop particles color");
    }
}