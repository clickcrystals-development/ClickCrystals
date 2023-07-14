package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.*;

public class BlankModule extends Module {

    private final SettingSection scGeneral = getGeneralSection(); // this is the default setting section
    private final SettingSection scTest = createSettingSection("hello-world"); // this is a new setting section
    public final ModuleSetting<Boolean> boolSetting = scGeneral.add(BooleanSetting.create() // this is a new setting
            .name("boolean-setting")
            .description("This is a boolean setting.")
            .def(false)
            .build()
    );
    public final ModuleSetting<String> strSetting = scGeneral.add(StringSetting.create()
            .name("string-setting")
            .description("This is a string setting.")
            .def("This is the default text. :)")
            .build()
    );
    public final ModuleSetting<Integer> intSettingNoMin = scGeneral.add(IntegerSetting.create()
            .max(10)
            .min(0)
            .name("integer-setting")
            .description("This is an integer setting.")
            .def(4)
            .build()
    );
    public final ModuleSetting<Integer> intSettingWithMin = scGeneral.add(IntegerSetting.create()
            .max(30)
            .min(5)
            .name("integer-setting-with-minimum")
            .description("This is an integer setting with a minimum value.")
            .def(10)
            .build()
    );
    public final ModuleSetting<Double> doubleSettingTwoDecimal = scTest.add(DoubleSetting.create()
            .max(101.21)
            .min(75.43)
            .decimalPlaces(2)
            .name("double-setting-two-decimals")
            .description("This is a double setting with two decimal places.")
            .def(87.34)
            .build()
    );
    public final ModuleSetting<Double> doubleSettingOneDecimal = scTest.add(DoubleSetting.create()
            .max(34.7)
            .decimalPlaces(1)
            .name("double-setting-one-decimals")
            .description("This is a double setting with one decimal places.")
            .def(5.67)
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
