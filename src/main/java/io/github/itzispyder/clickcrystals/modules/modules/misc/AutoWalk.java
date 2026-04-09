package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.client.input.KeyEvent;

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
            mc.options.keyUp.setDown(false);
            mc.options.keySprint.setDown(false);
        }
    }

    @EventHandler
    private void onKey(KeyPressEvent e) {
        if (e.isScreenNull()) {
            KeyEvent input = new KeyEvent(e.getKeycode(), e.getScancode(), 0);
            if (mc.options.keySprint.matches(input) ||
                    mc.options.keyUp.matches(input) ||
                    mc.options.keyDown.matches(input) ||
                    mc.options.keyLeft.matches(input) ||
                    mc.options.keyRight.matches(input)
            ) {
                e.cancel();
            }
        }
    }

    @EventHandler
    private void onTick(ClientTickStartEvent e) {
        mc.options.keyUp.setDown(true);
        if (shouldSprint.getVal()) {
            mc.options.keySprint.setDown(true);
        }
    }
}
