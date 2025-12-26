package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class BlockOutline extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Integer> red = scGeneral.add(createIntSetting()
            .name("Red")
            .description("Decide how much red will be on the color pattern.")
            .def(255)
            .max(255)
            .min(0)
            .build()
    );
    public final ModuleSetting<Integer> green = scGeneral.add(createIntSetting()
            .name("Green")
            .description("Decide how much green will be on the color pattern.")
            .def(255)
            .max(255)
            .min(0)
            .build()
    );
    public final ModuleSetting<Integer> blue = scGeneral.add(createIntSetting()
            .name("Blue")
            .description("Decide how much blue will be on the color pattern.")
            .def(255)
            .max(255)
            .min(0)
            .build()
    );

    public BlockOutline() {
        super("block-outline", Categories.RENDER, "Change the color of the block outline");
    }

    public int getRGB() {
        return red.getVal() << 16 | green.getVal() << 8 | blue.getVal();
    }
}
