package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class TimeChanger extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<TimeMode> timeMode = scGeneral.add(createEnumSetting(TimeMode.class)
            .name("time-mode")
            .description("Set the client world time.")
            .def(TimeMode.DAY)
            .build()
    );

    public TimeChanger() {
        super("time-changer", Categories.LAG, "Changes client world time.");
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
