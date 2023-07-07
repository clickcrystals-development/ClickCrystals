package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.gui.elements.ui.DetailedButtonElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.HyperLinkElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.ImageBannerElement;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

public class HomeScreen extends DefaultBase {

    public HomeScreen() {
        super("ClickCrystals Home Screen");
    }

    @Override
    protected void init() {
        Window win = mc.getWindow();
        int w = win.getScaledWidth();
        int h = win.getScaledHeight();
        int pageGap = 15;

        // banner
        ImageBannerElement banner = new ImageBannerElement(TexturesIdentifiers.SMOOTH_BANNER, 0, 0, base.width - 20, 0, "§l§oClickCrystals", "Your Ultimate CPvP Assistance", 2.0F);
        banner.resize(banner.width, (int)(banner.width * (90.0 / 512.0)));
        banner.centerIn(w, h);
        banner.moveTo(banner.x, base.y + 15);
        this.addChild(banner);

        Identifier bg = TexturesIdentifiers.SMOOTH_HORIZONTAL_WIDGET;
        // pages
        DetailedButtonElement modulesPage = new DetailedButtonElement(bg, banner.x + 25, banner.y + banner.height + pageGap, 100, 35, "ClickCrystals Modules", "Open up the modules toggle menu.", 0.8F, button -> {
            mc.setScreenAndRender(new ModulesScreen());
        });
        this.addChild(modulesPage);
        DetailedButtonElement searchPage = new DetailedButtonElement(bg, modulesPage.x + modulesPage.width + pageGap, modulesPage.y, 100, 35, "Search Modules", "Search existing ClickCrystals modules.", 0.8F, button -> {
            mc.setScreenAndRender(new SearchScreen());
        });
        this.addChild(searchPage);
        DetailedButtonElement discordPage = new DetailedButtonElement(bg, searchPage.x + searchPage.width + pageGap, modulesPage.y, 100, 35, "Join The Discord!", "", 0.8F);
        HyperLinkElement discordLink = new HyperLinkElement(discordPage.x + 15, discordPage.y + (int)(discordPage.height * 0.65), "https://discord.gg/tMaShNzNtP", "Join in the ClickCrystals community!", 0.4F, starter + "Join the Discord! >>>");
        discordPage.setPressAction(button -> discordLink.onClick(0, 0, 0));
        this.addChild(discordPage);
        this.addChild(discordLink);
        DetailedButtonElement githubPage = new DetailedButtonElement(bg, modulesPage.x, modulesPage.y + modulesPage.height + pageGap, 100, 35, "Open Source Code", "", 0.8F);
        HyperLinkElement githubLink = new HyperLinkElement(githubPage.x + 15, githubPage.y + (int)(discordPage.height * 0.65), "https://github.com/itzispyder/clickcrystals", "Check and read our source code!", 0.4F, starter + "Read our code!");
        githubPage.setPressAction(button -> githubLink.onClick(0, 0, 0));
        this.addChild(githubPage);
        this.addChild(githubLink);
        DetailedButtonElement modrinthPage = new DetailedButtonElement(bg, githubPage.x + githubPage.width + pageGap, githubPage.y, 100, 35, "Check for Updates!", "", 0.8F);
        HyperLinkElement modrinthLink = new HyperLinkElement(modrinthPage.x + 15, modrinthPage.y + (int)(discordPage.height * 0.65), "https://modrinth.com/mod/clickcrystals", "Check for new releases on Modrinth!", 0.4F, starter + "Go to Modrinth!");
        modrinthPage.setPressAction(button -> modrinthLink.onClick(0, 0, 0));
        this.addChild(modrinthPage);
        this.addChild(modrinthLink);
        DetailedButtonElement creditsPage = new DetailedButtonElement(bg, modrinthPage.x + modrinthPage.width + pageGap, githubPage.y, 100, 35, "Credits", "", 0.8F, button -> {

        });
        this.addChild(creditsPage);
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }
}
