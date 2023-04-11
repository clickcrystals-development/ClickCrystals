package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.SetScreenEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;

/**
 * NoLoadingScreen module
 */
public class NoLoadingScreen extends Module implements Listener {

    public NoLoadingScreen() {
        super("NoLoadingScreen", Categories.OPTIMIZATION,"Say goodbye to the annoying loading screens!");
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
        if (!(e.getScreen() instanceof ConnectScreen || e.getScreen() instanceof DownloadingTerrainScreen)) return;
        e.setCancelled(true);
    }
}
