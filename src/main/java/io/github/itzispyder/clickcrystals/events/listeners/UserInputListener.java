package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.gui.screens.*;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.DiscordRPC;
import io.github.itzispyder.clickcrystals.util.misc.ManualMap;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

import java.util.Map;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

public class UserInputListener implements Listener {

    public static final Map<Class<? extends Screen>, String> SCREEN_STATES = ManualMap.fromItems(
            TitleScreen.class, "Looking at the title screen",
            ModulesScreen.class, "Toggling modules",
            HomeScreen.class, "Scanning through ClickCrystals home",
            SearchScreen.class, "Searching modules",
            KeybindsScreen.class, "Changing ClickCrystals keybinds",
            SelectWorldScreen.class, "Selecting singleplayer",
            MultiplayerScreen.class, "Selecting server",
            GameMenuScreen.class, "Idling...",
            CreditsScreen.class, "Checking out the goats",
            BulletinScreen.class, "Viewing CC Bulletin Board"
    );

    @EventHandler
    public void onKeyPress(KeyPressEvent e) {
        try {
            this.handleKeybindings(e);
        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onScreenChange(SetScreenEvent e) {
        try {
            this.handleDiscordPresence(e);
            this.handleConfigSave(e);
        }
        catch (Exception ignore) {}
    }

    private void handleConfigSave(SetScreenEvent e) {
        if (e.getScreen() instanceof GameMenuScreen) {
            config.save();
        }
    }

    private void handleDiscordPresence(SetScreenEvent e) {
        Screen s = e.getScreen();

        if (s == null) {
            if (mc.player != null) {
                discordPresence.setState(mc.isInSingleplayer() ? "In singleplayer world" : "In multiplayer server");
            }
            else {
                discordPresence.setState("Looking at the title screen");
            }
        }
        else if (s instanceof ModuleSettingsScreen mss) {
            discordPresence.setState("Editing module " + mss.getModule().getName() + "...");
        }
        else {
            discordPresence.setState(SCREEN_STATES.getOrDefault(s.getClass(), "Clicking Crystals..."));
        }

        DiscordRPC.syncRPC();
    }

    private void handleKeybindings(KeyPressEvent e) {
        if (e.getAction() == ClickType.CLICK) {
            for (Keybind bind : system.getBindsOf(e.getKeycode())) {
                if (bind.canPress(e.getKeycode(), e.getScancode())) {
                    bind.onPress();
                }
            }
        }
    }
}
