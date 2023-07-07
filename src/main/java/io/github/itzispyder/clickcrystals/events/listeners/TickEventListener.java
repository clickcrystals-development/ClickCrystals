package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.client.CCKeybindings;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalsBase;
import io.github.itzispyder.clickcrystals.gui.screens.ModulesScreen;
import io.github.itzispyder.clickcrystals.scheduler.Scheduler;

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
            Scheduler.onTick();
        }
        catch (Exception ex) {}
    }

    private void handleScreenKeys() {
        if (CCKeybindings.OPEN_MODULE.wasPressed()) {
            ClickCrystalsBase.openClickCrystalsMenu();
            CCKeybindings.OPEN_MODULE.setPressed(false);
        }
    }
}
