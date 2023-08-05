package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.base.BackgroundElement;
import io.github.itzispyder.clickcrystals.gui.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

public abstract class DefaultBase extends GuiScreen {

    public BackgroundElement base = new BackgroundElement(0, 0, 400, 200);
    public WidgetElement baseShadow = new WidgetElement(0, 0, 400, 200);
    public ImageElement pepeUpdate = new ImageElement(GuiTextures.PEPE_UPDATE, 0, 0, 50, 50);

    public DefaultBase(String title) {
        super(title);
        this.baseInit();
    }

    private void baseInit() {
        Window win = mc.getWindow();
        int winW = win.getScaledWidth();
        int winH = win.getScaledHeight();

        base.centerIn(winW, winH);
        baseShadow.moveTo(base.x + 8, base.y + 8);
        pepeUpdate.moveTo(baseShadow.x + baseShadow.width - 98, base.y - 50);
        this.addChild(baseShadow);
        if (!ClickCrystals.matchLatestVersion()) {
            this.addChild(pepeUpdate);
        }
        this.addChild(base);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 0, 0xD0000000, 0xD03873A9);
    }
}
