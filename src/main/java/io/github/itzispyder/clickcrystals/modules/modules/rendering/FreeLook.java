package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.client.option.Perspective;

public class FreeLook extends DummyModule {
    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<POV> PerspectivePoint = scGeneral.add(EnumSetting.create(POV.class)
            .name("camera-perspective")
            .description("The Perspective Which Lock The Camera.")
            .def(POV.THIRD_PERSON_FOV)
            .build()
    );

    public FreeLook() {
        super("free-look", Categories.RENDER, "lock your camera perspective");
    }

    public enum POV {
        FIRST_PERSON_FOV(Perspective.FIRST_PERSON),
        SECOND_PERSON_FOV(Perspective.THIRD_PERSON_BACK),
        THIRD_PERSON_FOV(Perspective.THIRD_PERSON_FRONT);

        private final Perspective perspective;

        POV(Perspective perspective) {
            this.perspective = perspective;
        }

        public Perspective getPerspective() {
            return perspective;
        }
    }
}