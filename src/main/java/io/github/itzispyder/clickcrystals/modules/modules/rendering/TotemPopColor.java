package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

import java.awt.*;

public class TotemPopColor extends DummyModule {
    public TotemPopColor(){
        super("totem-color", Categories.RENDER, "Change totem pop particles color");
    }

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

    public final ModuleSetting<Double> alpha = scGeneral.add(createDoubleSetting()
            .name("Alpha")
            .description("Set transparency of the color.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );

    public Color getColor() {
        int r = red.getVal().intValue();
        int g = green.getVal().intValue();
        int b = blue.getVal().intValue();
        int a = alpha.getVal().intValue();
        return new Color(r, g, b, a);
    }
}