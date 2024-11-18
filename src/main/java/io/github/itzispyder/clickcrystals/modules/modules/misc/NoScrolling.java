package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseScrollEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class NoScrolling extends ListenerModule {
    public NoScrolling() {
        super("no-scrolling", Categories.MISC,"Disable hotbar scrolling");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> everyWhere = scGeneral.add(BooleanSetting.create()
            .name("disable-scrolling-entirely")
            .description("Disable scrolling in the whole game.")
            .def(false)
            .build()
    );

    @EventHandler()
    private void onScroll(MouseScrollEvent e){
        if (isEnabled() || everyWhere()) e.cancel();
    }

    public boolean everyWhere(){
        return mc.currentScreen == null && everyWhere.getVal() && isEnabled();
    }
}
