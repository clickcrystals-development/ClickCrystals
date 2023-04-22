package io.github.itzispyder.clickcrystals;

import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.commands.commands.*;
import io.github.itzispyder.clickcrystals.data.Configuration;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.events.listeners.ChatEventListener;
import io.github.itzispyder.clickcrystals.gui.hud.ClickCrystalIconHud;
import io.github.itzispyder.clickcrystals.gui.hud.ModuleListTextHud;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalMenuScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.io.File;

/**
 * ClickCrystals main
 */
public final class ClickCrystals implements ModInitializer {

    public static final File configFile = new File("ClickCrystalsClient/game_config.dat");
    public static final Configuration config = Configuration.load(configFile) != null ? Configuration.load(configFile) : new Configuration(configFile);
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final ClickCrystalsSystem system = new ClickCrystalsSystem();
    public static final ClickCrystalMenuScreen mainMenu = new ClickCrystalMenuScreen();

    @SuppressWarnings("unused")
    public static final String modId = "clickcrystals", prefix = "[ClickCrystals] ", starter = "§7[§bClick§3Crystals§7] §r";

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        // Mod initialization
        System.out.println(prefix + "Loading ClickCrystals by ImproperIssues");
        this.startTicking();
        this.init();

    }

    /**
     * Start click tick events
     */
    public void startTicking() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ClientTickStartEvent event = new ClientTickStartEvent();
            system.eventBus.pass(event);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientTickEndEvent event = new ClientTickEndEvent();
            system.eventBus.pass(event);
        });
    }

    public void init() {
        // Listeners
        system.addListener(new ChatEventListener());

        // Commands
        system.addCommand(new ClickCrystalToggleCommand());
        system.addCommand(new GmcCommand());
        system.addCommand(new GmsCommand());
        system.addCommand(new GmaCommand());
        system.addCommand(new GmspCommand());

        // Module
        system.addModule(new ClickCrystal());
        system.addModule(new GlowStoneSearch());
        system.addModule(new TpBlade());
        system.addModule(new NoBreakDelay());
        system.addModule(new FullBright());
        system.addModule(new NoHurtCam());
        system.addModule(new SlowHandSwing());
        system.addModule(new SpectatorSight());
        system.addModule(new NoLoadingScreen());
        system.addModule(new NoGameOverlay());
        system.addModule(new ObsidianSearch());
        system.addModule(new CrystalSearch());
        system.addModule(new NoServerParticles());
        system.addModule(new TpTotem());
        system.addModule(new NoResourcePack());
        system.addModule(new ToolSwitcher());
        system.addModule(new AnchorSearch());
        system.addModule(new ClickCrystalHud());
        system.addModule(new ModuleListHud());
        system.addModule(new SilkTouch());
        Module.loadConfigModules();

        // Hud
        system.addHud(new ClickCrystalIconHud());
        system.addHud(new ModuleListTextHud());
    }

    public void initRpc() {

    }
}
