package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.KeyPressEvent;
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
        system.getBindsOf(e.getKeycode()).forEach(Keybind::onPress);
    }
}
