package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseScrollEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import org.lwjgl.glfw.GLFW;

public class Zoom extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Double> multiplier = scGeneral.add(DoubleSetting.create()
            .max(3.0)
            .min(0.0)
            .name("Multiplier.")
            .description("Zoom fov value multiplier.")
            .def(0.3)
            .build()
    );
    public final ModuleSetting<Keybind> keybind = scGeneral.add(KeybindSetting.create()
            .name("zoom-key")
            .description("Keybind to activate zoom.")
            .def(GLFW.GLFW_KEY_B)
            .build()
    );
    public final ModuleSetting<Boolean> scrollToChange = scGeneral.add(BooleanSetting.create()
            .name("scroll-to-change")
            .description("Scroll to customize FOV (won't save to module settings)")
            .def(false)
            .build()
    );
    private boolean zooming;
    private int modifier;

    public Zoom() {
        super("zoom", Categories.MISC, "Changes your FOV");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onRelease(KeyPressEvent e) {
        if (mc.currentScreen != null) return;
        if (((KeybindSetting)keybind).getKey() == e.getKeycode()) {
            if (e.getAction().isDown()) {
                zooming = true;
                modifier = 0;
            }
            else if (e.getAction().isRelease()) {
                zooming = false;
                modifier = 0;
            }
        }
    }

    @EventHandler
    private void onScroll(MouseScrollEvent e) {
        if (mc.currentScreen != null) return;
        if (e.isVertical() && scrollToChange.getVal() && zooming) {
            modifier -= e.getDeltaYAsInt();
            e.cancel();
        }
    }

    public float getZoomMultiplierValue(double initValue) {
        return (float)(multiplier.getVal() * initValue + modifier);
    }

    public boolean isZooming() {
        return zooming;
    }
}
