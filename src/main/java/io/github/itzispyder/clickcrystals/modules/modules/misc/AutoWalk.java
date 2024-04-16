package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class AutoWalk extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> shouldSprint = scGeneral.add(createBoolSetting()
            .name("should-sprint")
            .description("Whether this module should allow the player to sprint.")
            .def(false)
            .build()
    );

    public AutoWalk() {
        super("auto-walk", Categories.MISC, "Presses the walk key for you (only useful in survival)");
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (mc != null && mc.options != null) {
            mc.options.forwardKey.setPressed(false);
            mc.options.sprintKey.setPressed(false);
        }
    }

    @EventHandler
    private void onKey(KeyPressEvent e) {
        if (e.isScreenNull()) {
            if (mc.options.sprintKey.matchesKey(e.getKeycode(), e.getScancode()) ||
                    mc.options.forwardKey.matchesKey(e.getKeycode(), e.getScancode()) ||
                    mc.options.backKey.matchesKey(e.getKeycode(), e.getScancode()) ||
                    mc.options.leftKey.matchesKey(e.getKeycode(), e.getScancode()) ||
                    mc.options.rightKey.matchesKey(e.getKeycode(), e.getScancode())
            ) {
                e.cancel();
            }
        }
    }

    @EventHandler
    private void onTick(ClientTickStartEvent e) {
        mc.options.forwardKey.setPressed(true);
        if (shouldSprint.getVal()) {
            mc.options.sprintKey.setPressed(true);
        }
    }
}
