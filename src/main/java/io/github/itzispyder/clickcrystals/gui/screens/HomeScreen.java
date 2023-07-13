package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.ui.DetailedButtonElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.ImageBannerElement;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

import static io.github.itzispyder.clickcrystals.ClickCrystals.*;

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
        ImageBannerElement banner = new ImageBannerElement(GuiTextures.SMOOTH_BANNER, 0, 0, base.width - 20, 0, "§l§oClickCrystals", "Your Ultimate CPvP Assistance", 2.0F);
        banner.resize(banner.width, (int)(banner.width * (90.0 / 512.0)));
        banner.centerIn(w, h);
        banner.moveTo(banner.x, base.y + 15);
        this.addChild(banner);

        Identifier bg = GuiTextures.HOLLOW_HORIZONTAL_WIDGET;
        // pages
        DetailedButtonElement modulesPage = DetailedButtonElement.builder()
                .texture(bg)
                .icon(GuiTextures.MODULES)
                .position(banner.x + 25, banner.y + banner.height + pageGap)
                .dimensions(100, 35)
                .title("View Modules")
                .subtitle("Open up the modules toggle menu.")
                .textScale(0.8F)
                .onPress(button -> mc.setScreenAndRender(new ModulesScreen()))
                .onBuild(HomeScreen.this::addChild)
                .build();

        DetailedButtonElement searchPage = DetailedButtonElement.builder()
                .texture(bg)
                .icon(GuiTextures.SEARCH)
                .position(modulesPage.x + modulesPage.width + pageGap, modulesPage.y)
                .dimensions(100, 35)
                .title("Search Modules")
                .subtitle("Search ClickCrystals modules.")
                .textScale(0.8F)
                .onPress(button -> mc.setScreenAndRender(new SearchScreen()))
                .onBuild(HomeScreen.this::addChild)
                .build();

        DetailedButtonElement discordPage = DetailedButtonElement.builder()
                .texture(bg)
                .icon(GuiTextures.DISCORD)
                .position(searchPage.x + searchPage.width + pageGap, modulesPage.y)
                .dimensions(100, 35)
                .title("Join the Discord!")
                .subtitle("§b§uJoin in the ClickCrystals community!")
                .textScale(0.8F)
                .onPress(btn -> {
                    try {
                        system.openUrl("https://discord.gg/tMaShNzNtP", starter + "Join the Discord! >>>", mc.currentScreen.getClass().newInstance());
                    }
                    catch (Exception ignore) {}
                })
                .onBuild(HomeScreen.this::addChild)
                .build();

        DetailedButtonElement githubPage = DetailedButtonElement.builder()
                .texture(bg)
                .icon(GuiTextures.CODE)
                .position(modulesPage.x, modulesPage.y + modulesPage.height + pageGap)
                .dimensions(100, 35)
                .title("Source Code")
                .subtitle("§b§uCheck for new releases on Modrinth!")
                .textScale(0.8F)
                .onPress(btn -> {
                    try {
                        system.openUrl("https://github.com/itzispyder/clickcrystals", starter + "Read our code!", mc.currentScreen.getClass().newInstance());
                    }
                    catch (Exception ignore) {}
                })
                .onBuild(HomeScreen.this::addChild)
                .build();

        DetailedButtonElement modrinthPage = DetailedButtonElement.builder()
                .texture(bg)
                .icon(GuiTextures.ICON)
                .position(githubPage.x + githubPage.width + pageGap, githubPage.y)
                .dimensions(100, 35)
                .title("Check Updates!")
                .subtitle("§b§uCheck for new releases on Modrinth!")
                .textScale(0.8F)
                .onPress(btn -> {
                    try {
                        system.openUrl("https://modrinth.com/mod/clickcrystals", starter + "Go to Modrinth!", mc.currentScreen.getClass().newInstance());
                    }
                    catch (Exception ignore) {}
                })
                .onBuild(HomeScreen.this::addChild)
                .build();

        DetailedButtonElement creditsPage = DetailedButtonElement.builder()
                .texture(bg)
                .icon(GuiTextures.PEOPLE)
                .position(modrinthPage.x + modrinthPage.width + pageGap, githubPage.y)
                .dimensions(100, 35)
                .title("Credits")
                .subtitle("Your Idols Fr")
                .textScale(0.8F)
                .onPress(btn -> mc.setScreenAndRender(new CreditsScreen()))
                .onBuild(HomeScreen.this::addChild)
                .build();
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }
}
