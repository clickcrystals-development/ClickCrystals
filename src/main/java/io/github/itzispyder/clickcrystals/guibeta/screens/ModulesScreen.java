package io.github.itzispyder.clickcrystals.guibeta.screens;

import io.github.itzispyder.clickcrystals.guibeta.GuiScreen;
import io.github.itzispyder.clickcrystals.guibeta.elements.BackgroundElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.BannerElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.WidgetElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ModulesScreen extends GuiScreen {

    public ModulesScreen() {
        super("ClickCrystals Modules Screen");
    }

    @Override
    protected void init() {
        Window win = mc.getWindow();
        int winW = win.getScaledWidth();
        int winH = win.getScaledHeight();

        BackgroundElement bg = new BackgroundElement(winW / 4, winH / 4, winW / 2, winH / 2);
        BannerElement title = new BannerElement("§fClickCrystals - by §bImproperIssues§f, §bTheTrouper", 1.0F, bg.x, bg.y + (int)(bg.height * 0.1), bg.width, (int)(bg.height * 0.1), 0xFF24A2A2);
        bg.addChild(title);
        this.addChild(bg);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {

    }
}
