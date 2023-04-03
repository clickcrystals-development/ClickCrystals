package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;

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
            MAX_ROW_PER_COLUMN = 6,
            MARGIN_TOP = 80,
            MARGIN_LEFT = 25,
            GAP = 3,
            BUTTON_HEIGHT = 18,
            BUTTON_WIDTH = 130;

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
        GridWidget grid = new GridWidget();
        GridWidget.Adder adder = grid.createAdder(2);

        TexturedButtonWidget title = new TexturedButtonWidget(
                25,
                25,
                256,
                64,
                0,
                0,
                0,
                SCREEN_TITLE_BANNER_TEXTURE,
                256,
                64,
                button -> {

                });
        adder.add(title,2);

        int y = 0, x = 0;
        List<Module> sortedModules = system.modules().values()
                .stream()
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        for (Module module : sortedModules) {
            if (y ++ >= MAX_ROW_PER_COLUMN) {
                x ++;
                y = 1;
            }

            ButtonWidget widget = ButtonWidget.builder(Text.literal(module.getCurrentStateLabel()), button -> {
                module.setEnabled(!module.isEnabled(),false);
                button.setMessage(Text.literal(module.getCurrentStateLabel()));
            }).position(MARGIN_LEFT + ((BUTTON_WIDTH + GAP) * x),MARGIN_TOP + ((BUTTON_HEIGHT + GAP) * y))
                    .size(BUTTON_WIDTH,BUTTON_HEIGHT)
                    .build();
            widget.setTooltip(Tooltip.of(Text.of(module.getHelp())));
            adder.add(widget);
        }

        super.addDrawableChild(grid);
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
