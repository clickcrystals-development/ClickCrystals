package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.gui.screen.Screen;

public class ScreenInitEvent extends Event {

    private final Screen screen;

    public ScreenInitEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
}
