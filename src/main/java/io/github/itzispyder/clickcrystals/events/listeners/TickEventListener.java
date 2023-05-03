package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalMenuScreen;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalsModuleScreen;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class TickEventListener implements Listener {

    @EventHandler
    public void onTickStart(ClientTickStartEvent e) {

    }

    @EventHandler
    public void onTickEnd(ClientTickEndEvent e) {

    }

    private void handleScreens(ClientTickEndEvent e) {
        if (ClickCrystalMenuScreen.KEY.wasPressed()) {
            mc.setScreenAndRender(new ClickCrystalMenuScreen());
            ClickCrystalMenuScreen.KEY.setPressed(false);
        }
        else if (ClickCrystalsModuleScreen.KEY.wasPressed()) {
            mc.setScreenAndRender(new ClickCrystalsModuleScreen());
            ClickCrystalsModuleScreen.KEY.setPressed(false);
        }
    }
}
