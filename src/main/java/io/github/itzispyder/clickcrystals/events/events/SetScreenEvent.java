package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.gui.screen.Screen;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Called when your client's screen changes
 */
public class SetScreenEvent extends Event implements Cancellable {

    private final Screen screen, previousScreen;
    private boolean cancelled;

    public SetScreenEvent(Screen screen) {
        this.previousScreen = mc.currentScreen;
        this.screen = screen;
        this.cancelled = false;
    }

    public Screen getScreen() {
        return screen;
    }

    public Screen getPreviousScreen() {
        return previousScreen;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
