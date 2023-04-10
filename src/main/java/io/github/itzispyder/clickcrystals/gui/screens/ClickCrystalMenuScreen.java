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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
    public static final String KEY_CATEGORY = "clickcrystals.category.main";
    public static final String KEY_TRANSLATION = "clickcrystals.menu.open_key";
    public static final KeyBinding KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            KEY_TRANSLATION,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_APOSTROPHE,
            KEY_CATEGORY
    ));
    public static final int
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
        mainGrid.getMainPositioner().margin(1,1,1,1);
        GridWidget.Adder mainAdder = mainGrid.createAdder(Categories.getCategories().size());

        IconWidget title = new IconWidget(0,0,256,64,SCREEN_TITLE_BANNER_TEXTURE);
        mainAdder.add(title,Categories.getCategories().size());

        Map<Category,Integer> categoryMap = new ManualMap<Category,Integer>(
                Categories.CRYSTALLING,0,
                Categories.ANCHORING,1,
                Categories.MISC,2,
                Categories.OPTIMIZATION,3,
                Categories.DETECTABLE,4
        ).getMap();

        categoryMap.forEach((category,i) -> {
            GridWidget catGrid = new GridWidget();
            catGrid.getMainPositioner().margin(1,1,1,1);
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
        SimplePositioningWidget.setPos(mainGrid,0,0,this.width,this.height,0.5F,0.25F);
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
