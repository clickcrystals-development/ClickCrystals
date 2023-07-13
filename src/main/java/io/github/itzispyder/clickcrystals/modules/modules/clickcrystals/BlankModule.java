package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.Setting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingGroup;

public class BlankModule extends Module {

    private final SettingGroup sgGeneral = getGeneralGroup();
    private final SettingGroup sgTest = createSettingGroup("hello-world");
    private final Setting<Boolean> goofy = sgGeneral.add(BooleanSetting.create()
            .name("goofy-setting")
            .description("This is a goofy setting from the testing module.")
            .def(false)
            .build()
    );
    private final Setting<Boolean> hello = sgTest.add(BooleanSetting.create()
            .name("hello-world-boolean")
            .description("Testing 123")
            .def(false)
            .build()
    );
    private final Setting<Boolean> hello2 = sgTest.add(BooleanSetting.create()
            .name("hello-world-2")
            .description("Shout out to HELLO WORLD, we love JAVA!")
            .def(false)
            .build()
    );

    public BlankModule() {
        super("test-module", Categories.CLICKCRYSTALS, "This module does nothing, serves as a testing purpose for development.");
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
