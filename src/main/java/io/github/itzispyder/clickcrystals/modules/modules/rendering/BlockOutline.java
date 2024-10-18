package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class BlockOutline extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Double> red = scGeneral.add(createDoubleSetting()
            .name("Red")
            .description("Decide how much red will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> green = scGeneral.add(createDoubleSetting()
            .name("Green")
            .description("Decide how much green will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> blue = scGeneral.add(createDoubleSetting()
            .name("Blue")
            .description("Decide how much blue will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );

    public BlockOutline() {
        super("block-outline", Categories.RENDER, "Change the color of the block outline");
    }

}
