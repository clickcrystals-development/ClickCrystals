package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.gui.elements.ui.DetailedButtonElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.HyperLinkElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.ImageBannerElement;
import net.minecraft.client.util.Window;

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

        // pages
        DetailedButtonElement modulesPage = new DetailedButtonElement(TexturesIdentifiers.HOLLOW_HORIZONTAL_WIDGET, banner.x + 25, banner.y + banner.height + pageGap, 100, 35, "ClickCrystals Modules", "Open up the modules toggle menu.", 0.8F, button -> {
            mc.setScreenAndRender(new ModulesScreen());
        });
        this.addChild(modulesPage);
        DetailedButtonElement searchPage = new DetailedButtonElement(TexturesIdentifiers.HOLLOW_HORIZONTAL_WIDGET, modulesPage.x + modulesPage.width + pageGap, modulesPage.y, 100, 35, "Search Modules", "Search existing ClickCrystals modules.", 0.8F, button -> {
            mc.setScreenAndRender(new SearchScreen());
        });
        this.addChild(searchPage);
        DetailedButtonElement discordPage = new DetailedButtonElement(TexturesIdentifiers.HOLLOW_HORIZONTAL_WIDGET, searchPage.x + searchPage.width + pageGap, modulesPage.y, 100, 35, "Join The Discord!", "", 0.8F);
        HyperLinkElement discordLink = new HyperLinkElement(discordPage.x + 15, discordPage.y + (int)(discordPage.height * 0.65), "https://discord.gg/tMaShNzNtP", "Join in the ClickCrystals community!", 0.4F, starter + "Join the Discord! >>>");
        discordPage.setPressAction(button -> discordLink.onClick(0, 0, 0));
        this.addChild(discordPage);
        this.addChild(discordLink);
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }
}
