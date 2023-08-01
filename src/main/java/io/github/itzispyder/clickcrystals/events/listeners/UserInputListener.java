package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class UserInputListener implements Listener {

    @EventHandler
    public void onKeyPress(KeyPressEvent e) {
        try {
            this.handleKeybindings(e);
        }
        catch (Exception ignore) {}
    }

    private void handleKeybindings(KeyPressEvent e) {
        if (e.getAction() == ClickType.CLICK) {
            for (Keybind bind : system.getBindsOf(e.getKeycode())) {
                if (bind.canPress(e.getKeycode(), e.getScancode())) {
                    bind.onPress();
                }
            }
        }
    }
}
