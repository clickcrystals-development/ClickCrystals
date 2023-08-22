package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.HyperLinkElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.ImageTabListElement;

import java.util.List;

import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;
import static io.github.itzispyder.clickcrystals.ClickCrystals.version;

public abstract class ClickCrystalsBase extends DefaultBase {

    private static Class<? extends DefaultBase> prevOpened = HomeScreen.class;
    private static int prevMenuSelection = 0;

    public WidgetElement nav = new WidgetElement(base.x + 10, base.y + 10, 80, base.height - 20, WidgetElement.Orientation.VERTICAL);
    public ImageElement ccIcon = new ImageElement(GuiTextures.ICON, nav.x + 3, nav.y + 5, 15, 15);
    public TextElement navTitle = new TextElement("ClickCrystals v" + version, TextAlignment.LEFT, 0.55F, ccIcon.x + ccIcon.width + 1, nav.y + 12);
    public ImageElement homeIcon = new ImageElement(GuiTextures.HOME, 0, 0, 0, 0);
    public ImageElement modulesIcon = new ImageElement(GuiTextures.MODULES, 0, 0, 0, 0);
    public ImageElement searchIcon = new ImageElement(GuiTextures.SEARCH, 0, 0, 0, 0);
    public ImageElement bulletinIcon = new ImageElement(GuiTextures.ANNOUNCEMENT, 0, 0, 0, 0);
    public ImageElement keyboardIcon = new ImageElement(GuiTextures.KEYBOARD, 0, 0, 0, 0);
    public ImageTabListElement menuTab = new ImageTabListElement(List.of(homeIcon, modulesIcon, searchIcon, bulletinIcon, keyboardIcon), nav.x + 3, navTitle.y + navTitle.height + 10, nav.width - 6, 13, button -> {
        ImageElement selection = button.getOptions().get(button.getSelection());
        prevMenuSelection = button.getSelection();

        if (selection == homeIcon)
            mc.setScreenAndRender(new HomeScreen());
        else if (selection == modulesIcon)
            mc.setScreenAndRender(new ModulesScreen());
        else if (selection == searchIcon)
            mc.setScreenAndRender(new SearchScreen());
        else if (selection == bulletinIcon)
            mc.setScreenAndRender(new BulletinScreen());
        else if (selection == keyboardIcon)
            mc.setScreenAndRender(new KeybindsScreen());
    });
    public HyperLinkElement discordLink = new HyperLinkElement(ccIcon.x, menuTab.y + menuTab.height + 5, "https://discord.gg/tMaShNzNtP", "Join Discord ⬀", 0.5F, starter + "Join the Discord! >>>");
    public HyperLinkElement githubLink = new HyperLinkElement(ccIcon.x, discordLink.y + discordLink.height + 5, "https://github.com/itzispyder/clickcrystals", "Open Source ⬀", 0.5F,  starter + "Check Our Code!");
    public HyperLinkElement modrinthLink = new HyperLinkElement(ccIcon.x, githubLink.y + githubLink.height + 5, "https://modrinth.com/mod/clickcrystals", "Modrinth Download ⬀", 0.5F, starter + "Follow our Project!");
    public HyperLinkElement itzispyderLink = new HyperLinkElement(ccIcon.x, modrinthLink.y + modrinthLink.height + 5, "https://itzispyder.github.io", "ImproperIssues ⬀", 0.5F, starter + "Read our bios!");
    public HyperLinkElement thetrouperLink = new HyperLinkElement(ccIcon.x, itzispyderLink.y + itzispyderLink.height + 5, "https://thetrouper.github.io", "obvWolf ⬀", 0.5F, starter + "Read our bios!");
    public TextElement copyright = new TextElement("§7Copyright (c) 2023 ClickCrystals", TextAlignment.LEFT, 0.4F, nav.x + 3, nav.y + nav.height - 10);
    public TextElement names = new TextElement("§bImproperIssues§7, §bobvWolf", TextAlignment.LEFT, 0.4F, nav.x + 3, copyright.y - 5);
    public TextElement credits = new TextElement("§7Client Owners:", TextAlignment.LEFT, 0.4F, nav.x + 3, copyright.y - 10);

    public ClickCrystalsBase(String title) {
        super(title);
        this.baseInit();
    }

    @SuppressWarnings("deprecation")
    public static void openClickCrystalsMenu() {
        try {
            mc.setScreenAndRender(prevOpened.newInstance());
        }
        catch (Exception ignore) {}
    }

    public static void setPrevOpened(Class<? extends DefaultBase> prevOpened) {
        ClickCrystalsBase.prevOpened = prevOpened;
    }

    private void baseInit() {
        nav.addChild(ccIcon);
        nav.addChild(navTitle);
        nav.addChild(copyright);
        nav.addChild(names);
        nav.addChild(credits);
        this.addChild(nav);
        menuTab.setSelection(prevMenuSelection);
        this.addChild(menuTab);
        this.addChild(discordLink);
        this.addChild(githubLink);
        this.addChild(modrinthLink);
        this.addChild(itzispyderLink);
        this.addChild(thetrouperLink);
    }
}
