package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.client.CCKeybindings;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.gui.widgets.CategoryWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

public class ClickCrystalsModuleScreen extends Screen implements Listener {

    public ClickCrystalsModuleScreen() {
        super(Text.literal("ClickCrystals Modules"));
        system.addListener(this);
    }

    @Override
    protected void init() {
        super.init();
        system.addListener(this);

        super.addDrawableChild(new CategoryWidget(10,10,100,100, Text.literal("ClickCrystals")));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fillGradient(matrices, 0, 0, this.width, this.height, 0x90050B0B, 0x903873A9);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (CCKeybindings.OPEN_MODULE.wasPressed()) {
            mc.setScreenAndRender(CC_MODULE_SCREEN);
            CCKeybindings.OPEN_MODULE.setPressed(false);
        }
    }
}
