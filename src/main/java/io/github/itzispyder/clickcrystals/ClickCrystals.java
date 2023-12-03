package io.github.itzispyder.clickcrystals;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsInfo;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.client.system.Version;
import io.github.itzispyder.clickcrystals.commands.commands.*;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.data.JsonSerializable;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.events.listeners.ChatEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.NetworkEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.hud.fixed.*;
import io.github.itzispyder.clickcrystals.gui.hud.moveables.*;
import io.github.itzispyder.clickcrystals.gui.screens.HudEditScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.*;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.*;
import io.github.itzispyder.clickcrystals.modules.modules.crystalling.*;
import io.github.itzispyder.clickcrystals.modules.modules.misc.*;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.*;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.*;
import io.github.itzispyder.clickcrystals.modules.scripts.*;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
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
 * TODO: (1) Update mod version down in "this file"
 * TODO: (2) Update mod "gradle.properties"
 * TODO: (4) Update "README.md"
 *
 * Publishing checklist
 * TODO: (5) Modrinth Release
 * TODO: (6) GitHub Release
 * TODO: (7) PlanetMC Release
 * TODO: (8) Update https://itzispyder.github.io/clickcrystals/info
 * TODO: (9) Discord Announcement
 */
public final class ClickCrystals implements ModInitializer {

    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public static final Config config = JsonSerializable.load(Config.PATH_CONFIG, Config.class, new Config());
    public static final Keybind openModuleKeybind = Keybind.create()
            .id("open-clickcrystals-module-screen")
            .defaultKey(GLFW.GLFW_KEY_APOSTROPHE)
            .condition((bind, screen) -> screen == null || screen instanceof TitleScreen || screen instanceof MultiplayerScreen || screen instanceof SelectWorldScreen)
            .onPress(bind -> UserInputListener.openPreviousScreen())
            .onChange(config::saveKeybind)
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
            .onChange(config::saveKeybind)
            .build();
    public static final Keybind commandPrefix = Keybind.create()
            .id("command-prefix")
            .defaultKey(GLFW.GLFW_KEY_COMMA)
            .condition((bind, screen) -> screen == null)
            .onPress(bind -> mc.setScreen(new ChatScreen("")))
            .onChange(config::saveKeybind)
            .build();

    /**
     * TODO: UPDATE THE MOD VERSION HERE!!!!!!
     * TODO: DON'T FORGET AGAIN!!!!
     */
    @SuppressWarnings("unused")
    public static final String modId   = "clickcrystals";
    public static final String prefix  = "[ClickCrystals] ";
    public static final String starter = "§7[§bClick§3Crystals§7] §r";
    public static final String version = "1.1.5";

    public static ClickCrystalsInfo info = new ClickCrystalsInfo(version);


    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        // Mod initialization
        system.println("Loading ClickCrystals by ImproperIssues");
        System.setProperty("java.awt.headless", "false");

        system.println("-> loading scripts...");
        this.initClickScript();
        system.println("-> initializing...");
        this.init();
        this.startTicking();
        system.println("-> requesting mod info...");
        this.requestModInfo();
        system.println("-> loading config...");
        config.loadEntireConfig();

        system.println("-> checking updates...");
        if (!matchLatestVersion()) {
            system.println("WARNING: You are running an outdated version of ClickCrystals, please update!");
            system.println("VERSIONS: Current=%s, Newest=%s".formatted(version, getLatestVersion()));
        }
        system.println("-> clicking crystals!");
        system.println("ClickCrystals had loaded successfully!");
        system.println(new Gson().toJson(info));
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

    public void initClickScript() {
        ClickScript.register(new ModuleCmd());
        ClickScript.register(new DescCmd());
        ClickScript.register(new OnEventCmd());
        ClickScript.register(new GuiSwitchCmd());
        ClickScript.register(new SwitchCmd());
        ClickScript.register(new SayCmd());
        ClickScript.register(new InputCmd());
        ClickScript.register(new IfNotCmd());
        ClickScript.register(new IfCmd());
        ClickScript.register(new WaitRandomCmd());
        ClickScript.register(new WaitCmd());
        ClickScript.register(new SendCmd());
        ClickScript.register(new SwapCmd());
        ClickScript.register(new TurnToCmd());
        ClickScript.register(new LoopPeriodCmd());
        ClickScript.register(new DropCmd());
        ClickScript.register(new ConfigCmd());
        ClickScript.register(new NotifyCmd());
        ScriptedModule.runModuleScripts();
    }

    public void init() {
        // Listeners
        system.addListener(new ChatEventListener());
        system.addListener(new NetworkEventListener());
        system.addListener(new TickEventListener());
        system.addListener(new UserInputListener());

        // Module
        this.initModules();

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
        system.addCommand(new RotateCommand());
        system.addCommand(new LookCommand());
        system.addCommand(new ClickScriptCommand());

        // Hud
        system.addHud(new ColorOverlayHud());
        system.addHud(new ModuleListTextHud());
        system.addHud(new ClickPerSecondHud());
        system.addHud(new ArmorItemHud());
        system.addHud(new NotificationHud());

        system.addHud(new IconRelativeHud());
        system.addHud(new PingRelativeHud());
        system.addHud(new FpsRelativeHud());
        system.addHud(new ClockRelativeHud());
        system.addHud(new TargetRelativeHud());
        system.addHud(new PosRelativeHud());
        system.addHud(new BiomeRelativeHud());
        system.addHud(new DirectionRelativeHud());
        system.addHud(new CrosshairTargetRelativeHud());
        system.addHud(new RotationRelativeHud());
        system.addHud(new ResourceRelativeHud());
    }

    public void initModules() {
        // anchors
        system.addModule(new AxeSwap());
        system.addModule(new ShieldSwitch());
        system.addModule(new SwordSwap());
        system.addModule(new GapSwap());
        system.addModule(new RailSwap());
        system.addModule(new TntSwap());
        system.addModule(new BowSwap());

        // client
        system.addModule(new GuiBorders());
        system.addModule(new InGameHuds());
        system.addModule(new SilkTouch());
        system.addModule(new EntityStatuses());

        // crystalling
        system.addModule(new CrystAnchor());
        system.addModule(new AnchorSwitch());
        system.addModule(new ClickCrystal());
        system.addModule(new ClientCryst());
        system.addModule(new CrystSwitch());
        system.addModule(new ObiSwitch());
        system.addModule(new PearlSwitch());
        system.addModule(new GuiCursor());

        // misc
        system.addModule(new ArmorHud());
        system.addModule(new AutoGG());
        system.addModule(new AutoRespawn());
        system.addModule(new CrystPerSec());
        system.addModule(new ModulesList());
        system.addModule(new MsgResend());
        system.addModule(new ToolSwitcher());
        system.addModule(new TotemPops());
        system.addModule(new ChatPrefix());
        system.addModule(new NextBlock());
        system.addModule(new AutoWalk());
        system.addModule(new NoBreakDelay());
        system.addModule(new MouseTaper());

        // optimization
        system.addModule(new AntiCrash());
        system.addModule(new ExplodeParticles());
        system.addModule(new NoItemBounce());
        system.addModule(new NoLoading());
        system.addModule(new NoResPack());
        system.addModule(new FullBright());

        // rendering
        system.addModule(new SlowSwing());
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

    public static boolean matchLatestVersion() {
        return Version.ofString(version).isUpToDate(getLatestVersion());
    }

    public static Version getLatestVersion() {
        return info.getLatest();
    }

    private void requestModInfo() {
        ClickCrystalsInfo.request();
        system.capeManager.reloadTextures();
    }
}
///fr 
