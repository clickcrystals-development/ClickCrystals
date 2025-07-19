package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class CameraClip extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> enableCameraClip = scGeneral.add(createBoolSetting()
            .name("camera-clip")
            .description("Set the camera to ignore walls when it encounters them.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Double> clipDistance = scGeneral.add(createDoubleSetting()
            .name("camera-distance")
            .description("The distance from the player to the camera.")
            .def(5.0)
            .max(100.0)
            .min(0.0)
            .build()
    );

    public CameraClip() {
        super("camera-clip", Categories.MISC, "Change the camera distance and the camera behavior when encounter walls");
    }
}
