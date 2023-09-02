package io.github.itzispyder.clickcrystals;

import io.github.itzispyder.clickcrystals.client.client.CCSoundEvents;
import io.github.itzispyder.clickcrystals.client.client.CapeManager;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsInfo;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.client.system.DiscordPresence;
import io.github.itzispyder.clickcrystals.client.system.Version;
import io.github.itzispyder.clickcrystals.commands.commands.*;
import io.github.itzispyder.clickcrystals.data.ConfigFile;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.events.listeners.ChatEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.NetworkEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.hud.fixed.ArmorItemHud;
import io.github.itzispyder.clickcrystals.gui.hud.fixed.ClickPerSecondHud;
import io.github.itzispyder.clickcrystals.gui.hud.fixed.ColorOverlayHud;
import io.github.itzispyder.clickcrystals.gui.hud.fixed.ModuleListTextHud;
import io.github.itzispyder.clickcrystals.gui.hud.moveables.*;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalsBase;
import io.github.itzispyder.clickcrystals.gui.screens.HudEditScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.*;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.*;
import io.github.itzispyder.clickcrystals.modules.modules.crystalling.*;
import io.github.itzispyder.clickcrystals.modules.modules.minecart.BowSwap;
import io.github.itzispyder.clickcrystals.modules.modules.minecart.RailSwap;
import io.github.itzispyder.clickcrystals.modules.modules.minecart.TntSwap;
import io.github.itzispyder.clickcrystals.modules.modules.misc.*;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.*;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.*;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.lwjgl.glfw.GLFW;

/**
 * ClickCrystals main
 * TODO: (1) Update mod version down in "this file"
 * TODO: (2) Update mod "gradle.properties"
 * TODO: (3) Update mod version in "GitHub Pages"
 * TODO: (4) Update "README.md"
 */
public final class ClickCrystals implements ModInitializer, ClientLifecycleEvents.ClientStopping {

    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public static final CapeManager capeManager = new CapeManager();
    public static final ConfigFile config = ConfigFile.load("ClickCrystalsClient/config.json");
    public static final DiscordPresence discordPresence = new DiscordPresence();
    public static Thread discordWorker;
    public static final Keybind openModuleKeybind = Keybind.create()
            .id("open-clickcrystals-module-screen")
            .defaultKey(GLFW.GLFW_KEY_APOSTROPHE)
            .condition((bind, screen) -> screen == null || screen instanceof TitleScreen || screen instanceof MultiplayerScreen || screen instanceof SelectWorldScreen)
            .onPress(bind -> ClickCrystalsBase.openClickCrystalsMenu())
            .onChange(ClickCrystals::saveBind)
            .build();

    public static final Keybind openHudEditorKeybind = Keybind.create()
            .id("open-hud-editor-screen")
            .defaultKey(GLFW.GLFW_KEY_SEMICOLON)
            .condition((bind, screen) -> screen == null)
            .onPress(bind -> {
                if (Module.isEnabled(InGameHuds.class)) {
                    mc.setScreenAndRender(new HudEditScreen());
                }
                else {
                    ChatUtils.sendPrefixMessage("§cThe module §7InGameHuds §cis not enabled! Press this keybind again when it is.");
                }
            })
            .onChange(ClickCrystals::saveBind)
            .build();
    public static final Keybind commandPrefix = Keybind.create()
            .id("command-prefix")
            .defaultKey(GLFW.GLFW_KEY_COMMA)
            .condition((bind, screen) -> screen == null)
            .onPress(bind -> mc.setScreen(new ChatScreen("")))
            .onChange(ClickCrystals::saveBind)
            .build();

    /**
     * TODO: UPDATE THE MOD VERSION HERE!!!!!!
     * TODO: DON'T FORGET AGAIN!!!!
     */
    @SuppressWarnings("unused")
    public static final String
            modId = "clickcrystals",
            prefix = "[ClickCrystals] ",
            starter = "§7[§bClick§3Crystals§7] §r",
            version = "1.0.1";

    public static ClickCrystalsInfo info = new ClickCrystalsInfo(version);


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
        this.requestModInfo();
        this.initRpc();

        if (!matchLatestVersion()) {
            System.out.println(prefix + "WARNING: You are running an outdated version of ClickCrystals, please update!");
            System.out.println(prefix + "VERSIONS: Current=" + version + ", Newest=" + getLatestVersion());
        }
    }

    @Override
    public void onClientStopping(MinecraftClient client) {
        discordPresence.stop();
        config.save();
        Module.saveConfigModules();
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
        this.initModules();
        Module.loadConfigModules();

        // Commands
        system.addCommand(new CCToggleCommand());
        system.addCommand(new CCHelpCommand());
        system.addCommand(new GmcCommand());
        system.addCommand(new GmsCommand());
        system.addCommand(new GmaCommand());
        system.addCommand(new GmspCommand());
        system.addCommand(new CCDebugCommand());
        system.addCommand(new PixelArtCommand());
        system.addCommand(new KeybindsCommand());

        // Hud
        system.addHud(new ColorOverlayHud());
        system.addHud(new ModuleListTextHud());
        system.addHud(new ClickPerSecondHud());
        system.addHud(new ArmorItemHud());

        system.addHud(new IconRelativeHud());
        system.addHud(new PingRelativeHud());
        system.addHud(new FpsRelativeHud());
        system.addHud(new ClockRelativeHud());
        system.addHud(new TargetRelativeHud());
        system.addHud(new PosRelativeHud());
        system.addHud(new BiomeRelativeHud());
        system.addHud(new DirectionRelativeHud());
        system.addHud(new CrosshairTargetRelativeHud());
    }

    public void initModules() {
        // anchors
        system.addModule(new AnchorSwitch());
        system.addModule(new AxeSwap());
        system.addModule(new CrystAnchor());
        system.addModule(new ShieldSwitch());
        system.addModule(new SwordSwap());

        // client
        system.addModule(new CCExtras());
        system.addModule(new DiscordRPC());
        system.addModule(new GuiBorders());
        system.addModule(new InGameHuds());
        system.addModule(new SilkTouch());

        // crystalling
        system.addModule(new ClickCrystal());
        system.addModule(new ClientCryst());
        system.addModule(new CrystSwitch());
        system.addModule(new ObiSwitch());
        system.addModule(new PearlSwitch());
        system.addModule(new GuiCursor());

        // minecart
        system.addModule(new RailSwap());
        system.addModule(new TntSwap());
        system.addModule(new BowSwap());

        // misc
        system.addModule(new ArmorHud());
        system.addModule(new AutoGG());
        system.addModule(new AutoRespawn());
        system.addModule(new CrystPerSec());
        system.addModule(new ModulesList());
        system.addModule(new MsgResend());
        system.addModule(new SlowSwing());
        system.addModule(new ToolSwitcher());
        system.addModule(new TotemPops());
        system.addModule(new ChatPrefix());

        // optimization
        system.addModule(new AntiCrash());
        system.addModule(new ExplodeParticles());
        system.addModule(new NoItemBounce());
        system.addModule(new NoLoading());
        system.addModule(new NoResPack());

        // rendering
        system.addModule(new FullBright());
        system.addModule(new NoHurtCam());
        system.addModule(new NoOverlay());
        system.addModule(new TotemOverlay());
        system.addModule(new RenderOwnName());
        system.addModule(new NoViewBob());
        system.addModule(new GlowingEntities());
        system.addModule(new NoScoreboard());
        system.addModule(new HealthAsBar());
        system.addModule(new Zoom());
        system.addModule(new ViewModel());
        system.addModule(new GhostTotem());
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

    public static boolean matchLatestVersion() {
        return Version.ofString(version).isUpToDate(getLatestVersion());
    }

    public static Version getLatestVersion() {
        return info.getLatest();
    }

    private void requestModInfo() {
        ClickCrystalsInfo.request();
        capeManager.reloadTextures();
    }

    public void initRpc() {
        discordWorker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                discordPresence.api.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ignore) {}
            }
        });
        discordWorker.start();
    }
}
