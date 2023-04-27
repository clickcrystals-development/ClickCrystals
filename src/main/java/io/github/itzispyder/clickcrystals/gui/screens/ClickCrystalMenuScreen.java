package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ManualMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.Map;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

/**
 * Represents the module toggle screen for ClickCrystals
 */
@Environment(EnvType.CLIENT)
public class ClickCrystalMenuScreen extends Screen implements Listener {

    public static final Identifier SCREEN_TITLE_BANNER_TEXTURE = new Identifier(modId, "textures/gui/screen_title_banner.png");
    public static final Identifier SOCIAL_GITHUB_TEXTURE = new Identifier(modId, "textures/gui/social_github.png");
    public static final Identifier SOCIAL_DISCORD_TEXTURE = new Identifier(modId, "textures/gui/social_discord.png");
    public static final Identifier SOCIAL_MODRINTH_TEXTURE = new Identifier(modId, "textures/gui/social_modrinth.png");
    public static final Identifier SOCIAL_WEBSITE_TEXTURE = new Identifier(modId, "textures/gui/social_website.png");
    public static final String KEY_CATEGORY = "clickcrystals.category.main";
    public static final String KEY_TRANSLATION = "clickcrystals.key.open_menu";
    public static final KeyBinding KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            KEY_TRANSLATION,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_APOSTROPHE,
            KEY_CATEGORY
    ));
    public static final int
            GAP = 1,
            BUTTON_HEIGHT = 14,
            BUTTON_WIDTH = 90;

    /**
     * Constructs a new menu screen for the module screen
     */
    public ClickCrystalMenuScreen() {
        super(Text.literal("ClickCrystals - Simple and Based Utility Mod"));
        system.addListener(this);
    }

    /**
     * Initialize the module toggle screen
     */
    @Override
    public void init() {
        super.init();
        GridWidget mainGrid = new GridWidget();
        mainGrid.getMainPositioner().margin(GAP,GAP,GAP,GAP);
        GridWidget.Adder mainAdder = mainGrid.createAdder(Categories.getCategories().size());

        IconWidget title = new IconWidget(0,0,256,64,SCREEN_TITLE_BANNER_TEXTURE);
        mainAdder.add(title,Categories.getCategories().size());

        GridWidget socGrid = new GridWidget();
        socGrid.getMainPositioner().margin(8,0,0,0);
        GridWidget.Adder socAdder = socGrid.createAdder(1);

        socAdder.add(new TexturedButtonWidget(0,0,80,20,0,0,0,SOCIAL_GITHUB_TEXTURE,80,20,(button) -> {
            mc.setScreen(new ConfirmLinkScreen((confirmed) -> {
                if (confirmed) Util.getOperatingSystem().open("https://github.com/itzispyder/clickcrystals");
                mc.setScreen(ClickCrystalMenuScreen.this);
            },Text.literal(starter + "§bVisiting Github link"),"https://github.com/itzispyder/clickcrystals",true));
        }));
        socAdder.add(new TexturedButtonWidget(0,0,80,20,0,0,0,SOCIAL_DISCORD_TEXTURE,80,20,(button) -> {
            mc.setScreen(new ConfirmLinkScreen((confirmed) -> {
                if (confirmed) Util.getOperatingSystem().open("https://discord.gg/tMaShNzNtP");
                mc.setScreen(ClickCrystalMenuScreen.this);
            },Text.literal(starter + "§bVisiting Discord link"),"https://discord.gg/tMaShNzNtP",true));
        }));
        socAdder.add(new TexturedButtonWidget(0,0,80,20,0,0,0,SOCIAL_MODRINTH_TEXTURE,80,20,(button) -> {
            mc.setScreen(new ConfirmLinkScreen((confirmed) -> {
                if (confirmed) Util.getOperatingSystem().open("https://modrinth.com/mod/clickcrystals");
                mc.setScreen(ClickCrystalMenuScreen.this);
            },Text.literal(starter + "§bVisiting Modrinth link"),"https://modrinth.com/mod/clickcrystals",true));
        }));
        socAdder.add(new TexturedButtonWidget(0,0,80,20,0,0,0,SOCIAL_WEBSITE_TEXTURE,80,20,(button) -> {
            mc.setScreen(new ConfirmLinkScreen((confirmed) -> {
                if (confirmed) Util.getOperatingSystem().open("https://itzispyder.github.io/clickcrystals");
                mc.setScreen(ClickCrystalMenuScreen.this);
            },Text.literal(starter + "§bVisiting Website link"),"https://github.com/ItziSpyder/ClickCrystals",true));
        }));

        socGrid.refreshPositions();
        mainGrid.add(socGrid,0,Categories.getCategories().size() - 1);

        Map<Category,Integer> categoryMap = new ManualMap<Category,Integer>(
                Categories.CRYSTALLING,0,
                Categories.ANCHORING,1,
                Categories.MISC,2,
                Categories.OPTIMIZATION,3,
                Categories.OTHER,4
        ).getMap();

        categoryMap.forEach((category,i) -> {
            GridWidget catGrid = new GridWidget();
            catGrid.getMainPositioner().margin(GAP,0,GAP,0);
            GridWidget.Adder catAdder = catGrid.createAdder(1);

            catAdder.add(category.getTextureWidget());

            system.modules().values().stream()
                    .sorted(Comparator.comparing(Module::getId))
                    .filter(module -> module.getCategory().equals(category))
                    .forEach(module -> {
                        catAdder.add(ButtonWidget.builder(Text.literal(module.getCurrentStateLabel()), (button) -> {
                            module.setEnabled(!module.isEnabled(),false);
                            button.setMessage(Text.literal(module.getCurrentStateLabel()));
                        }).size(BUTTON_WIDTH,BUTTON_HEIGHT)
                        .tooltip(Tooltip.of(Text.literal(module.getHelp())))
                        .build());
                    });

            catGrid.refreshPositions();
            mainGrid.add(catGrid,1,i);
        });

        mainGrid.refreshPositions();
        SimplePositioningWidget.setPos(mainGrid,0,0,this.width,this.height,0.5F,0.1F);
        mainGrid.forEachChild(this::addDrawableChild);
    }

    /**
     * On screen tick
     */
    @Override
    public void tick() {
        super.tick();
    }

    /**
     * On screen render
     * @param matrices matrices
     * @param mouseX mouse x
     * @param mouseY mouse y
     * @param delta delta value
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    /**
     * Checks when the key is pressed, if so, then open the menu
     * @param e client tick event
     */
    @EventHandler
    private void onClientTick(ClientTickEndEvent e) {
        if (KEY.isPressed()) {
            mc.setScreenAndRender(this);
            KEY.setPressed(false);
        }
    }
}
