package io.github.itzispyder.clickcrystals.gui.screens;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickCrystalsModuleScreen extends Screen {

    public static final String
            KEY_TRANSLATION = "clickcrystals.key.open_modules",
            KEY_CATEGORY = "clickcrystals.category.main";
    public static final KeyBinding KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            KEY_TRANSLATION,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_SHIFT,
            KEY_CATEGORY
    ));

    public ClickCrystalsModuleScreen() {
        super(Text.literal("ClickCrystals Modules"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fillGradient(matrices, 0,0, this.width,this.height, 2903419, 2911355);
        super.render(matrices, mouseX, mouseY, delta);

        DrawableHelper.drawBorder(matrices, 0,0, 100,100, 16777215);
    }
}
