package io.github.itzispyder.clickcrystals.guibeta.screens;

import io.github.itzispyder.clickcrystals.guibeta.ClickType;
import io.github.itzispyder.clickcrystals.guibeta.GuiScreen;
import io.github.itzispyder.clickcrystals.guibeta.TextAlignment;
import io.github.itzispyder.clickcrystals.guibeta.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.BackgroundElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.cc.ModuleElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.ui.NoticeWidget;
import io.github.itzispyder.clickcrystals.guibeta.elements.ui.TabListElement;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class ModulesScreen extends GuiScreen {

    private final List<ModuleElement> displayingModules = new ArrayList<>();
    public NoticeWidget alertWidget;

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
        TextElement navTitle = new TextElement("ClickCrystals v0.8.7", TextAlignment.LEFT, 0.55F, ccIcon.x + ccIcon.width + 1, nav.y + 12);
        TextElement copyright = new TextElement("§7Copyright (c) 2023 ClickCrystals", TextAlignment.LEFT, 0.4F, nav.x + 3, nav.y + nav.height - 10);
        TextElement names = new TextElement("§bImproperIssues§7, §bTheTrouper", TextAlignment.LEFT, 0.4F, nav.x + 3, copyright.y - 5);
        TextElement credits = new TextElement("§7Client Owners:", TextAlignment.LEFT, 0.4F, nav.x + 3, copyright.y - 10);
        nav.addChild(ccIcon);
        nav.addChild(navTitle);
        nav.addChild(copyright);
        nav.addChild(names);
        nav.addChild(credits);
        this.addChild(nav);

        // category bar
        TabListElement<Category> catBar = new TabListElement<>(Categories.getCategories().values().stream().toList(),nav.x + nav.width + 10, base.y + 10, (base.x + base.width) - (nav.x + nav.width + 20), 25, tabs -> {
            this.setCategory(tabs.getOptions().get(tabs.getSelection()), nav.x + nav.width + 10, tabs.y + tabs.height + 10);
        }, Category::name);
        this.addChild(catBar);

        // modules settings
        this.setCategory(catBar.getOptions().get(catBar.getSelection()), nav.x + nav.width + 10, catBar.y + catBar.height + 10);
        this.screenRenderListeners.add((context, mouseX, mouseY, delta) -> displayingModules.forEach(moduleElement -> {
            moduleElement.render(context, mouseX, mouseY);
        }));
        this.mouseClickListeners.add(((mouseX, mouseY, button, click) -> displayingModules.forEach(element -> {
            if (click == ClickType.CLICK && element.isHovered((int)mouseX, (int)mouseY)) {
                element.onClick(mouseX, mouseY, button);
            }
        })));

        // module description
        this.alertWidget = new NoticeWidget("", "");
        alertWidget.setRendering(false);
        this.screenRenderListeners.add((context, mouseX, mouseY, delta) -> {
            alertWidget.render(context, mouseX, mouseY);
        });
        this.keyActionListeners.add((key, click, scancode, modifiers) -> {
            if (alertWidget.rendering) alertWidget.onClick(0, 0, 0);
        });

        // finish
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 0, 0xD0000000, 0xD03873A9);
    }

    public void setCategory(Category category, int originX, int originY) {
        displayingModules.clear();

        List<Module> modules = system.modules().values().stream()
                .filter(m -> m.getCategory() == category)
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        int row, column;
        row = column = 0;

        for (Module module : modules) {
            ModuleElement me = new ModuleElement(module, 0, 0, 60);
            me.setX(originX + (me.width + 5) * column);
            me.setY(originY + (me.height + 3) * row);

            displayingModules.add(me);

            if (++column >= 4) {
                column = 0;
                row ++;
            }
        }
    }
}
