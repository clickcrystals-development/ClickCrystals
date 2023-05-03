package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.client.CCKeybindings;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
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

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        if (CCKeybindings.OPEN_MODULE.wasPressed()) {
            mc.setScreenAndRender(CC_MODULE_SCREEN);
            CCKeybindings.OPEN_MODULE.setPressed(false);
        }
    }
}
