package io.github.itzispyder.clickcrystals;

import io.github.itzispyder.clickcrystals.client.CCSoundEvents;
import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.commands.commands.*;
import io.github.itzispyder.clickcrystals.data.ConfigFile;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.events.listeners.ChatEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.NetworkEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.hud.*;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalsBase;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.*;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.CCExtras;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.GuiBorders;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.IconHud;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SilkTouch;
import io.github.itzispyder.clickcrystals.modules.modules.crystalling.*;
import io.github.itzispyder.clickcrystals.modules.modules.misc.*;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.AntiCrash;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.NoItemBounce;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.NoLoading;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.NoResPack;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.lwjgl.glfw.GLFW;

/**
 * ClickCrystals main
 */
public final class ClickCrystals implements ModInitializer {

    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public static final ConfigFile config = ConfigFile.load("ClickCrystalsClient/config.json");
    public static final Keybind openModuleKeybind = Keybind.create()
            .id("open-clickcrystals-module-screen")
            .defaultKey(GLFW.GLFW_KEY_RIGHT_SHIFT)
            .condition((bind, screen) -> screen == null || screen instanceof TitleScreen || screen instanceof MultiplayerScreen || screen instanceof SelectWorldScreen)
            .onPress(bind -> ClickCrystalsBase.openClickCrystalsMenu())
            .onChange(ClickCrystals::saveBind)
            .build();
    public static final Keybind commandPrefix = Keybind.create()
            .id("command-prefix")
            .defaultKey(GLFW.GLFW_KEY_APOSTROPHE)
            .condition((bind, screen) -> screen == null)
            .onPress(bind -> mc.setScreen(new ChatScreen("")))
            .onChange(ClickCrystals::saveBind)
            .build();

    @SuppressWarnings("unused")
    public static final String
            modId = "clickcrystals",
            prefix = "[ClickCrystals] ",
            starter = "§7[§bClick§3Crystals§7] §r",
            version = "0.9.4";

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        // Mod initialization
        System.out.println(prefix + "Loading ClickCrystals by ImproperIssues");
        this.init();
        CCSoundEvents.init();
        this.startTicking();
        this.initOther();
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
        system.addListener(new NetworkEventListener());
        system.addListener(new TickEventListener());
        system.addListener(new UserInputListener());

        // Module
        system.addModule(new ClickCrystal());
        system.addModule(new AnchorSwitch());
        system.addModule(new PearlSwitch());
        system.addModule(new FullBright());
        system.addModule(new NoHurtCam());
        system.addModule(new SlowSwing());
        system.addModule(new NoLoading());
        system.addModule(new NoOverlay());
        system.addModule(new ObiSwitch());
        system.addModule(new CrystSwitch());
        system.addModule(new AntiCrash());
        system.addModule(new NoResPack());
        system.addModule(new ToolSwitcher());
        system.addModule(new CrystAnchor());
        system.addModule(new IconHud());
        system.addModule(new ModulesList());
        system.addModule(new SilkTouch());
        system.addModule(new TotemPops());
        system.addModule(new CrystPerSec());
        system.addModule(new MsgResend());
        system.addModule(new CCExtras());
        system.addModule(new TotemOverlay());
        system.addModule(new ShieldSwitch());
        system.addModule(new RenderOwnName());
        system.addModule(new AutoRespawn());
        system.addModule(new NoViewBob());
        system.addModule(new ClientCryst());
        system.addModule(new NoItemBounce());
        system.addModule(new GuiBorders());
        system.addModule(new SwordSwap());
        system.addModule(new AxeSwap());
        system.addModule(new GlowingEntities());
        system.addModule(new AutoGG());
        system.addModule(new NoScoreboard());
        system.addModule(new ArmorHud());
        Module.loadConfigModules();

        // Commands
        system.addCommand(new CCToggleCommand());
        system.addCommand(new CCHelpCommand());
        system.addCommand(new GmcCommand());
        system.addCommand(new GmsCommand());
        system.addCommand(new GmaCommand());
        system.addCommand(new GmspCommand());
        system.addCommand(new CCDebugCommand());

        // Hud
        system.addHud(new ColorOverlayHud());
        system.addHud(new ClickCrystalsIconHud());
        system.addHud(new ModuleListTextHud());
        system.addHud(new ClickPerSecondHud());
        system.addHud(new ArmorItemHud());
    }

    public void initOther() {
        // keybind setting
        loadBind(openModuleKeybind);
        loadBind(commandPrefix);
    }

    public static void saveBind(Keybind bind) {
        config.set(bind.getId(), bind.getKey());
        config.save();
    }

    public static void loadBind(Keybind bind) {
        double val = config.get(bind.getId(), Double.class, (double)bind.getDefaultKey());
        int key = (int)val;
        bind.setKey(key);
    }
}
