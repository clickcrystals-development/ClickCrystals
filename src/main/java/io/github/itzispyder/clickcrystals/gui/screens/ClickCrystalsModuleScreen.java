package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.client.CCKeybindings;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.gui.widgets.BoxWidget;
import io.github.itzispyder.clickcrystals.gui.widgets.TestWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

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

        super.addDrawableChild(new TestWidget(100,100,256,64,Text.literal("banner")));
        super.addDrawableChild(new BoxWidget(50,50,64,64,Text.literal("testbox")));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //super.renderBackground(matrices);
        final int startColor = ColorHelper.Argb.getArgb(1, 0, 57, 94);
        final int endColor = ColorHelper.Argb.getArgb(1, 51, 91, 132);
        DrawableHelper.fillGradient(matrices,0,0, this.width, this.height, startColor, endColor);
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
