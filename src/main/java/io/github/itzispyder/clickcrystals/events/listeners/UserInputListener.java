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
import io.github.itzispyder.clickcrystals.gui.screens.profiles.DownloadProfileScreen;
import io.github.itzispyder.clickcrystals.gui.screens.profiles.ProfilesScreen;
import io.github.itzispyder.clickcrystals.gui.screens.scripts.ClickScriptIDE;
import io.github.itzispyder.clickcrystals.gui.screens.scripts.DownloadScriptScreen;
import io.github.itzispyder.clickcrystals.gui.screens.settings.AdvancedSettingScreen;
import io.github.itzispyder.clickcrystals.gui.screens.settings.InfoScreen;
import io.github.itzispyder.clickcrystals.gui.screens.settings.KeybindScreen;
import io.github.itzispyder.clickcrystals.gui.screens.settings.SettingScreen;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.ManualMap;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

import static io.github.itzispyder.clickcrystals.ClickCrystals.config;

public class UserInputListener implements Listener {

    private static final ConcurrentLinkedQueue<QueuedGuiItemSearchListener> guiItemSearchQueue = new ConcurrentLinkedQueue<>();
    private static final Set<Class<? extends GuiScreen>> moduleScreenRoot = new HashSet<>() {{
        add(BrowsingScreen.class);
        add(OverviewScreen.class);
        add(ClickScriptIDE.class);
    }};
    private static final Map<Class<? extends GuiScreen>, Class<? extends GuiScreen>> screenTracker = ManualMap.fromItems(
            SearchScreen.class, SearchScreen.class,
            SettingScreen.class, SettingScreen.class,
            BulletinScreen.class, BulletinScreen.class,
            HomeScreen.class, HomeScreen.class,
            AdvancedSettingScreen.class, AdvancedSettingScreen.class,
            KeybindScreen.class, KeybindScreen.class,
            InfoScreen.class, InfoScreen.class,
            ProfilesScreen.class, ProfilesScreen.class,
            DownloadProfileScreen.class, DownloadProfileScreen.class,
            DownloadScriptScreen.class, DownloadScriptScreen.class,
            ModuleEditScreen.class, ModuleEditScreen.class
    );
    private static Class<? extends GuiScreen> previousScreen = null;
    private static final Set<Integer> pressedKeys = new HashSet<>();

    @SuppressWarnings("deprecation")
    public static void openPreviousScreen() {
        config.markPlayedBefore();
        if (previousScreen == null || !(screenTracker.containsKey(previousScreen) || moduleScreenRoot.contains(previousScreen))) {
            scheduleOpenScreen(new HomeScreen());
            return;
        }
        if (moduleScreenRoot.contains(previousScreen)) {
            scheduleOpenScreen(PlayerUtils.valid() && config.isOverviewMode() ? new OverviewScreen() : new BrowsingScreen());
            return;
        }

        try {
            scheduleOpenScreen(previousScreen.newInstance());
        }
        catch (Exception e) {
            scheduleOpenScreen(new HomeScreen());
        }
    }

    public static boolean isScreenTracked(Screen screen) {
        return screen instanceof GuiScreen gui && (screenTracker.containsKey(gui.getClass()) || moduleScreenRoot.contains(gui.getClass()));
    }

    public static void openModulesScreen() {
        if (PlayerUtils.valid() && config.isOverviewMode()) {
            mc.setScreen(new OverviewScreen());
            return;
        }
        mc.setScreen(new BrowsingScreen());
    }

    public static void scheduleOpenScreen(GuiScreen screen) {
        if (screen instanceof AnimatedBase base) {
            boolean bl = PlayerUtils.valid();
            base.setPlayOpenAnimation(bl);
            base.setPlayCloseAnimation(bl);
        }
        mc.execute(() -> mc.setScreen(screen));
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent e) {
        try {
            this.handleRecordKeys(e);
            this.handleKeybindings(e);
        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onScreenChange(SetScreenEvent e) {
        try {
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
        if (e.getScreen() instanceof BrowsingScreen && PlayerUtils.valid() && config.isOverviewMode()) {
            mc.setScreen(new OverviewScreen());
            return;
        }
        if (e.getScreen() == null && isScreenTracked(e.getPreviousScreen())) {
            previousScreen = ((GuiScreen) e.getPreviousScreen()).getClass();
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

    private void handleKeybindings(KeyPressEvent e) {
        if (e.getAction() == ClickType.CLICK) {
            for (Keybind bind : system.getBindsOf(e.getKeycode())) {
                if (bind.canPress(e.getKeycode(), e.getScancode())) {
                    bind.onPress();
                }
            }
        }
    }

    private void handleRecordKeys(KeyPressEvent e) {
        switch (e.getAction()) {
            case CLICK -> pressedKeys.add(e.getKeycode());
            case RELEASE -> pressedKeys.remove(e.getKeycode());
        }
    }

    public static boolean isKeyPressed(int key) {
        return pressedKeys.contains(key);
    }


    private static class QueuedGuiItemSearchListener {
        private Predicate<ItemStack> item;

        public QueuedGuiItemSearchListener(Predicate<ItemStack> item) {
            this.item = item;
        }

        public void accept(RenderInventorySlotEvent e) {
            if (!item.test(e.getItem()))
                return;

            InteractionUtils.setCursor(e.getX() + 8, e.getY() + 8);
            item = null;
            guiItemSearchQueue.remove(this);
        }
    }
}
