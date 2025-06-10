package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.MathUtils;

public class TimeChanger extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
//    public final ModuleSetting<TimeMode> timeMode = scGeneral.add(createEnumSetting(TimeMode.class)
//            .name("time-mode")
//            .description("Set the client world time.")
//            .def(TimeMode.DAY)
//            .build()
//    );
    public final ModuleSetting<Double> time = scGeneral.add(createDoubleSetting()
        .name("time")
        .description("Set the client world time")
        .decimalPlaces(2)
        .max(1.0)
        .min(0.0)
        .def(0.0)
        .build()
    );

    public TimeChanger() {
        super("time-changer", Categories.LAG, "Changes client world time");
    }

    public long getTime() {
//        return timeMode.getVal().getTime();
        return (long) MathUtils.lerp(0L, 18000L, time.getVal());
    }

    public enum TimeMode {
        DAY(1000L),
        NOON(6000L),
        NIGHT(13000L),
        MIDNIGHT(18000L);

        private final long time;

        TimeMode(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }
    }
}
