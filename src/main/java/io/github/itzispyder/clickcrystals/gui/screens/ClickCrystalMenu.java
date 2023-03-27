package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEvent;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

@Environment(EnvType.CLIENT)
public class ClickCrystalMenu extends Screen implements Listener {

    public static final Identifier SCREEN_TITLE_BANNER_TEXTURE = new Identifier(modId, "textures/gui/screen_title_banner.png");
    public static final String KEY_CATEGORY = "clickcrystals.category.main";
    public static final String KEY_TRANSLATION = "clickcrystals.menu.open_key";
    public static final KeyBinding KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            KEY_TRANSLATION,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_APOSTROPHE,
            KEY_CATEGORY
    ));

    public ClickCrystalMenu() {
        super(Text.literal("ClickCrystals - Simple and Based Utility Mod"));
        system.addListener(this);
    }

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

        int i = 0;
        for (Module module : system.modules().values()) {
            i ++;
            ButtonWidget widget = ButtonWidget.builder(Text.literal(module.getCurrentStateLabel()), button -> {
                module.setEnabled(!module.isEnabled(),false);
                button.setMessage(Text.literal(module.getCurrentStateLabel()));
            }).position(25,80 + (25 * i)).build();
            widget.setTooltip(Tooltip.of(Text.of(module.getHelp())));
            adder.add(widget);
        }

        super.addDrawableChild(grid);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @EventHandler
    private void onClientTick(ClientTickEvent.End e) {
        if (KEY.isPressed()) {
            mc.setScreenAndRender(this);
            KEY.setPressed(false);
        }
    }
}
