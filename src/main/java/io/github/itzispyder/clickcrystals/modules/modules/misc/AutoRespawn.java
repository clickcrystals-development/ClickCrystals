package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;

public class AutoRespawn extends Module implements Listener {

    public AutoRespawn() {
        super("auto-respawn", Categories.MISC, "Clicks the respawn button for you");
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
    public void onDeathScreen(SetScreenEvent e) {
        if (PlayerUtils.invalid())
            return;

        if (e.getScreen() instanceof DeathScreen) {
            mc.player.requestRespawn();
            mc.setScreen((Screen)null);
        }
    }
}
