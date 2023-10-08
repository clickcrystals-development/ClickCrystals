package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;

public abstract class ListenerModule extends Module implements Listener {

    public ListenerModule(String name, Category category, String description) {
        super(name, category, description);
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }
}
