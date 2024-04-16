package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;

public class NoLoading extends Module implements Listener {

    public NoLoading() {
        super("no-load-screen", Categories.LAG,"Prevents some loading screens from rendering");
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
    private void onScreenChange(SetScreenEvent e) {
        if (e.getScreen() instanceof DownloadingTerrainScreen) {
            e.setCancelled(true);
        }
    }
}
