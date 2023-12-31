package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class TotemPopScale extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Double> scale = scGeneral.add(createDoubleSetting()
            .name("scale")
            .description("Scale to render the totem pop floating item.")
            .def(1.0)
            .max(3.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Integer> translateX = scGeneral.add(createIntSetting()
            .name("x-translation")
            .description("Shift totem pop along the x axis.")
            .def(0)
            .max(128)
            .min(-128)
            .build()
    );
    public final ModuleSetting<Integer> translateY = scGeneral.add(createIntSetting()
            .name("y-translation")
            .description("Shift totem pop along the y axis.")
            .def(0)
            .max(128)
            .min(-128)
            .build()
    );

    public TotemPopScale() {
        super("totem-scale", Categories.RENDER, "Changes the scale of the totem pop.");
    }
}
