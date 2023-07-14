package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.*;

public class BlankModule extends Module {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scTest = createSettingSection("hello-world");
    public final ModuleSetting<Boolean> goofy = scGeneral.add(BooleanSetting.create()
            .name("goofy-setting")
            .description("This is a goofy setting from the testing module.")
            .def(false)
            .build()
    );
    public final ModuleSetting<String> anotherString = scGeneral.add(StringSetting.create()
            .name("lmfao-real")
            .description("weeeeeeeee")
            .def("KYS (keep urself safe)")
            .build()
    );
    public final ModuleSetting<String> anotherAnotherString = scGeneral.add(StringSetting.create()
            .name("lmfao-real")
            .description("weeeeeeeee")
            .def("KYS (keep urself safe)")
            .build()
    );
    public final ModuleSetting<String> anotherStringAnother = scGeneral.add(StringSetting.create()
            .name("lmfao-real")
            .description("weeeeeeeee")
            .def("KYS (keep urself safe)")
            .build()
    );
    public final ModuleSetting<Integer> anotherInt = scGeneral.add(IntegerSetting.create()
            .max(10)
            .min(0)
            .name("testing-integer-setting")
            .description("this knob does nothing haha lol")
            .def(4)
            .build()
    );
    public final ModuleSetting<Integer> coolInt = scGeneral.add(IntegerSetting.create()
            .max(30)
            .min(5)
            .name("testing-integer-setting-again")
            .description("this knob does nothing haha lol again")
            .def(10)
            .build()
    );
    public final ModuleSetting<Boolean> hello = scTest.add(BooleanSetting.create()
            .name("hello-world-boolean")
            .description("Testing 123")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> hello2 = scTest.add(BooleanSetting.create()
            .name("hello-world-2")
            .description("Shout out to HELLO WORLD, we love JAVA!")
            .def(false)
            .build()
    );
    public final ModuleSetting<Double> yoYoYo = scTest.add(DoubleSetting.create()
            .max(101.21)
            .min(75.43)
            .decimalPlaces(2)
            .name("yo-yo-yo-test")
            .description("Wassup")
            .def(87.34)
            .build()
    );
    public final ModuleSetting<Double> yoYoYo2 = scTest.add(DoubleSetting.create()
            .max(34.7)
            .decimalPlaces(1)
            .name("yo-yo-yo-test-secondary")
            .description("Wassup [x2]")
            .def(5.67)
            .build()
    );
    public final ModuleSetting<String> mailBox = scTest.add(StringSetting.create()
            .name("your-mail-box")
            .description("You have mail!")
            .def("Hello World")
            .build()
    );
    public final ModuleSetting<String> eee = scTest.add(StringSetting.create()
            .name("eee")
            .description("eeeeeeeeeeeeeeeeeeeeeeeeeee")
            .def("Hello World with E's")
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
