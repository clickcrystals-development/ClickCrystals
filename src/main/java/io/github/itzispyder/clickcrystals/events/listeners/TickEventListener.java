package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.client.CCKeybindings;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.ClientTickStartEvent;

import static io.github.itzispyder.clickcrystals.ClickCrystals.CC_MODULE_SCREEN;
import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class TickEventListener implements Listener {

    @EventHandler
    public void onTickStart(ClientTickStartEvent e) {
        try {

        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onTickEnd(ClientTickEndEvent e) {
        try {
            this.handleScreenKeys();
        }
        catch (Exception ex) {}
    }

    private void handleScreenKeys() {
        if (CCKeybindings.OPEN_MODULE.wasPressed()) {
            mc.setScreenAndRender(CC_MODULE_SCREEN);
            CCKeybindings.OPEN_MODULE.setPressed(false);
        }
    }
}
