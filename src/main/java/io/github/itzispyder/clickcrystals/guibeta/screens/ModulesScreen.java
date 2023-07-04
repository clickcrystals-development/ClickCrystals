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
import io.github.itzispyder.clickcrystals.guibeta.elements.ui.HyperLinkElement;
import io.github.itzispyder.clickcrystals.guibeta.elements.ui.TabListElement;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

public class ModulesScreen extends GuiScreen {

    private final List<ModuleElement> displayingModules = new ArrayList<>();

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
        HyperLinkElement discordLink = new HyperLinkElement(ccIcon.x, ccIcon.y + ccIcon.height + 5, "https://discord.gg/tMaShNzNtP", "Join Discord ⬀", 0.5F, starter + "Join the Discord! >>>");
        HyperLinkElement githubLink = new HyperLinkElement(ccIcon.x, discordLink.y + discordLink.height + 5, "https://github.com/clickcrystals", "Open Source ⬀", 0.5F,  starter + "Check Our Code!");
        HyperLinkElement modrinthLink = new HyperLinkElement(ccIcon.x, githubLink.y + githubLink.height + 5, "https://modrinth.com/mod/clickcrystals", "Modrinth Download ⬀", 0.5F, starter + "Follow our Project!");
        HyperLinkElement itzispyderLink = new HyperLinkElement(ccIcon.x, modrinthLink.y + modrinthLink.height + 5, "https://itzispyder.github.io", "ImproperIssues ⬀", 0.5F, starter + "Read our bios!");
        HyperLinkElement thetrouperLink = new HyperLinkElement(ccIcon.x, itzispyderLink.y + itzispyderLink.height + 5, "https://thetrouper.github.io", "obvWolf ⬀", 0.5F, starter + "Read our bios!");
        TextElement navTitle = new TextElement("ClickCrystals v" + version, TextAlignment.LEFT, 0.55F, ccIcon.x + ccIcon.width + 1, nav.y + 12);
        TextElement copyright = new TextElement("§7Copyright (c) 2023 ClickCrystals", TextAlignment.LEFT, 0.4F, nav.x + 3, nav.y + nav.height - 10);
        TextElement names = new TextElement("§bImproperIssues§7, §bobvWolf", TextAlignment.LEFT, 0.4F, nav.x + 3, copyright.y - 5);
        TextElement credits = new TextElement("§7Client Owners:", TextAlignment.LEFT, 0.4F, nav.x + 3, copyright.y - 10);
        nav.addChild(ccIcon);
        nav.addChild(navTitle);
        nav.addChild(copyright);
        nav.addChild(names);
        nav.addChild(credits);
        this.addChild(nav);
        this.addChild(discordLink);
        this.addChild(githubLink);
        this.addChild(modrinthLink);
        this.addChild(itzispyderLink);
        this.addChild(thetrouperLink);

        // category bar
        TabListElement<Category> catBar = new TabListElement<>(Categories.getCategories().values().stream().toList(),nav.x + nav.width + 10, base.y + 10, (base.x + base.width) - (nav.x + nav.width + 20), 25, tabs -> {
            this.setCategory(tabs.getOptions().get(tabs.getSelection()), nav.x + nav.width + 10, tabs.y + tabs.height + 10);
        }, Category::name);
        this.setCategory(catBar.getOptions().get(catBar.getSelection()), nav.x + nav.width + 10, catBar.y + catBar.height + 10);
        this.addChild(catBar);

        // callbacks
        this.screenRenderListeners.add((context, mouseX, mouseY, delta) -> displayingModules.forEach(moduleElement -> {
            moduleElement.render(context, mouseX, mouseY);
            Module m = moduleElement.getModule();

            if (moduleElement.isHovered(mouseX, mouseY)) {
                DrawableUtils.drawText(context, starter + m.getName(), nav.x + 3, copyright.y - 55, 0.5F, true);
                int i = 0;
                for (String line : StringUtils.wrapLines(m.getDescription(), 20, true)) {
                    DrawableUtils.drawText(context, "§7" + line, nav.x + 3, copyright.y - 50 + (i++ * 5), 0.48F, true);
                }
            }
        }));
        this.mouseClickListeners.add(((mouseX, mouseY, button, click) -> displayingModules.forEach(element -> {
            if (click == ClickType.CLICK && element.isHovered((int)mouseX, (int)mouseY)) {
                element.onClick(mouseX, mouseY, button);
            }
        })));

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
                row++;
            }
        }
    }
}
