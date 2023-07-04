package io.github.itzispyder.clickcrystals.guibeta.screens;

import io.github.itzispyder.clickcrystals.guibeta.GuiScreen;
import io.github.itzispyder.clickcrystals.guibeta.TextAlignment;
import io.github.itzispyder.clickcrystals.guibeta.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.BackgroundElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.ui.TabListElement;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
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

        // start
        BackgroundElement base = new BackgroundElement(0, 0, 400, 200);
        WidgetElement baseShadow = new WidgetElement(0, 0, 400, 200);
        base.centerIn(winW, winH);
        baseShadow.moveTo(base.x + 8, base.y + 8);
        this.addChild(baseShadow);
        this.addChild(base);

        // navbar
        WidgetElement nav = new WidgetElement(base.x + 10, base.y + 10, 80, base.height - 20, WidgetElement.Orientation.VERTICAL);
        ImageElement ccIcon = new ImageElement(TexturesIdentifiers.ICON_TEXTURE, nav.x + 3, nav.y + 5, 15, 15);
        TextElement navTitle = new TextElement("ClickCrystals v0.8.7", TextAlignment.LEFT, 0.5F, ccIcon.x + ccIcon.width + 1, nav.y + 12);
        nav.addChild(ccIcon);
        nav.addChild(navTitle);
        this.addChild(nav);

        // category bar
        TabListElement<Category> catBar = new TabListElement<>(Categories.getCategories().values().stream().toList(),nav.x + nav.width + 10, base.y + 10, (base.x + base.width) - (nav.x + nav.width + 20), 25, (category, integer) -> {

        }, Category::name);
        this.addChild(catBar);

        // finish
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 0, 0xD0000000, 0xD03873A9);
    }
}
