package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.RenderInventorySlotEvent;
import io.github.itzispyder.clickcrystals.events.events.client.SetScreenEvent;
import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.screens.*;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.gui.screens.settings.AdvancedSettingScreen;
import io.github.itzispyder.clickcrystals.gui.screens.settings.KeybindScreen;
import io.github.itzispyder.clickcrystals.gui.screens.settings.SettingScreen;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.DiscordRPC;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.misc.ManualMap;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

public class UserInputListener implements Listener {

    private static Class<? extends GuiScreen> previousScreen = null;
    public static void openPreviousScreen() {
        Class<? extends GuiScreen> p = previousScreen;
        if (p == BrowsingScreen.class || p == OverviewScreen.class || p == ModuleEditScreen.class || p == ClickScriptIDE.class) {
            openModulesScreen();
        }
        else if (p == SearchScreen.class) {
            mc.setScreen(new SearchScreen());
        }
        else if (p == SettingScreen.class) {
            mc.setScreen(new SettingScreen());
        }
        else if (p == BulletinScreen.class) {
            mc.setScreen(new BulletinScreen());
        }
        else {
            mc.setScreen(new HomeScreen());
        }
        config.markPlayedBefore();
    }

    public static void openModulesScreen() {
        if (mc.player != null && mc.world != null && config.isOverviewMode()) {
            mc.setScreen(new OverviewScreen());
            return;
        }
        mc.setScreen(new BrowsingScreen());
    }

    public static final Map<Class<? extends Screen>, String> SCREEN_STATES = ManualMap.fromItems(
            TitleScreen.class, "Looking at the title screen",
            BrowsingScreen.class, "Toggling modules",
            OverviewScreen.class, "Overviewing modules",
            HomeScreen.class, "Scanning through ClickCrystals home",
            SearchScreen.class, "Searching modules",
            SettingScreen.class, "Editing ClickCrystals settings",
            KeybindScreen.class, "Changing ClickCrystals keybinds",
            AdvancedSettingScreen.class, "Editing ClickCrystals advanced settings",
            SelectWorldScreen.class, "Selecting single player",
            MultiplayerScreen.class, "Selecting server",
            GameMenuScreen.class, "Idling...",
            ClickScriptIDE.class, "Creating a custom module...",
            BulletinScreen.class, "Viewing CC Bulletin Board"
    );

    private static final ConcurrentLinkedQueue<QueuedGuiItemSearchListener> guiItemSearchQueue = new ConcurrentLinkedQueue<>();

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
            this.handleScreenManagement(e);
        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onScreenRenderItem(RenderInventorySlotEvent e) {
        guiItemSearchQueue.forEach(q -> q.accept(e));
    }

    public static void queueGuiItemSearch(Predicate<ItemStack> item) {
        var q = new QueuedGuiItemSearchListener(item);
        guiItemSearchQueue.add(q);
        system.scheduler.runDelayedTask(() -> guiItemSearchQueue.remove(q), 50);
    }

    private void handleScreenManagement(SetScreenEvent e) {
        if (e.getScreen() instanceof BrowsingScreen && mc.player != null && mc.world != null && config.isOverviewMode()) {
            mc.setScreen(new OverviewScreen());
            return;
        }
        if (e.getScreen() == null && e.getPreviousScreen() instanceof GuiScreen screen) {
            Class<? extends GuiScreen> p = screen.getClass();

            if (p == BulletinScreen.class ||
                    p == ModuleEditScreen.class ||
                    p == ClickScriptIDE.class ||
                    p == SearchScreen.class ||
                    p == SettingScreen.class ||
                    p == HomeScreen.class ||
                    p == OverviewScreen.class ||
                    p == BrowsingScreen.class) {
                previousScreen = p;
            }
        }
    }

    private void handleConfigSave(SetScreenEvent e) {
        if (e.getScreen() instanceof GameMenuScreen) {
            system.println("<- saving data...");
            config.saveKeybinds();
            config.saveHuds();
            config.saveModules();
            system.println("<- saving config...");
            config.save();
        }
    }

    private void handleDiscordPresence(SetScreenEvent e) {
        if (discordPresence.loadedSuccessfully) {
            Screen s = e.getScreen();

            if (s == null) {
                if (mc.player != null) {
                    discordPresence.setState(mc.isInSingleplayer() ? "In singleplayer world" : "In multiplayer server");
                }
                else {
                    discordPresence.setState("Looking at the title screen");
                }
            }
            else if (s instanceof ModuleEditScreen mss) {
                discordPresence.setState("Editing module " + mss.getModule().getName() + "...");
            }
            else {
                discordPresence.setState(SCREEN_STATES.getOrDefault(s.getClass(), "Clicking Crystals..."));
            }

            DiscordRPC.syncRPC();
        }
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

    private static class QueuedGuiItemSearchListener {
        private long sequence;
        private Predicate<ItemStack> item;

        public QueuedGuiItemSearchListener(Predicate<ItemStack> item) {
            this.item = item;
        }

        public void accept(RenderInventorySlotEvent e) {
            if (item.test(e.getItem())) {
                InteractionUtils.setCursor(e.getX() + 8, e.getY() + 8);
                sequence = 0;
                item = null;
                guiItemSearchQueue.remove(this);
                return;
            }

            if (sequence++ >= 45) { // all inv slots
                guiItemSearchQueue.remove(this);
            }
        }
    }
}
