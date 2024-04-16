package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class ViewModel extends Module {

    private final SettingSection scMain = createSettingSection("main-hand");
    public final ModuleSetting<Double> mainRotX = scMain.add(DoubleSetting.create()
            .max(360)
            .min(-360)
            .decimalPlaces(1)
            .name("main-rotation-x")
            .description("Rotation X axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> mainRotY = scMain.add(DoubleSetting.create()
            .max(360)
            .min(-360)
            .decimalPlaces(1)
            .name("main-rotation-y")
            .description("Rotation Y axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> mainRotZ = scMain.add(DoubleSetting.create()
            .max(360)
            .min(-360)
            .decimalPlaces(1)
            .name("main-rotation-z")
            .description("Rotation Z axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> mainPosX = scMain.add(DoubleSetting.create()
            .max(3)
            .min(-3)
            .decimalPlaces(1)
            .name("main-position-x")
            .description("Position X axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> mainPosY = scMain.add(DoubleSetting.create()
            .max(3)
            .min(-3)
            .decimalPlaces(1)
            .name("main-position-y")
            .description("Position Y axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> mainPosZ = scMain.add(DoubleSetting.create()
            .max(3)
            .min(-3)
            .decimalPlaces(1)
            .name("main-position-z")
            .description("Position Z axis.")
            .def(0.0)
            .build()
    );
    private final SettingSection scOff = createSettingSection("off-hand");
    public final ModuleSetting<Double> offRotX = scOff.add(DoubleSetting.create()
            .max(360)
            .min(-360)
            .decimalPlaces(1)
            .name("off-rotation-x")
            .description("Rotation X axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> offRotY = scOff.add(DoubleSetting.create()
            .max(360)
            .min(-360)
            .decimalPlaces(1)
            .name("off-rotation-y")
            .description("Rotation Y axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> offRotZ = scOff.add(DoubleSetting.create()
            .max(360)
            .min(-360)
            .decimalPlaces(1)
            .name("off-rotation-z")
            .description("Rotation Z axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> offPosX = scOff.add(DoubleSetting.create()
            .max(3)
            .min(-3)
            .decimalPlaces(1)
            .name("off-position-x")
            .description("Position X axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> offPosY = scOff.add(DoubleSetting.create()
            .max(3)
            .min(-3)
            .decimalPlaces(1)
            .name("off-position-y")
            .description("Position Y axis.")
            .def(0.0)
            .build()
    );
    public final ModuleSetting<Double> offPosZ = scOff.add(DoubleSetting.create()
            .max(3)
            .min(-3)
            .decimalPlaces(1)
            .name("off-position-z")
            .description("Position Z axis.")
            .def(0.0)
            .build()
    );

    public ViewModel() {
        super("view-model", Categories.RENDER, "Changes your view model in your first person hand view");
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
