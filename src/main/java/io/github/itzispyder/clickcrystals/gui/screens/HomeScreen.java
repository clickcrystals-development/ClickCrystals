package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.ui.DetailedButtonElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.ImageBannerElement;
import io.github.itzispyder.clickcrystals.gui.organizers.GridOrganizer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

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

        // banner
        ImageBannerElement banner = new ImageBannerElement(GuiTextures.SMOOTH_BANNER, 0, 0, base.width - 20, 0, "§l§oClickCrystals", "Your Ultimate CPvP Assistance", 2.0F);
        banner.resize(banner.width, (int)(banner.width * (90.0 / 512.0)));
        banner.centerIn(w, h);
        banner.moveTo(banner.x, base.y + 15);
        this.addChild(banner);

        Identifier bg = GuiTextures.HOLLOW_HORIZONTAL_WIDGET;
        // pages
        int gap = 15;
        w = 100;
        h = 35;
        int x = banner.x + 25;
        int y = banner.y + banner.height + gap;
        GridOrganizer grid = new GridOrganizer(x, y, w, h, 3, gap);

        DetailedButtonElement.create()
                .texture(bg)
                .icon(GuiTextures.MODULES)
                .dimensions(w, h)
                .title("View modules")
                .subtitle("Module view page")
                .textScale(0.8F)
                .onPress(button -> mc.setScreenAndRender(new ModulesScreen()))
                .onBuild(grid::addEntry)
                .build();

        DetailedButtonElement.create()
                .texture(bg)
                .icon(GuiTextures.SEARCH)
                .dimensions(w, h)
                .title("Search modules")
                .subtitle("Search modules...")
                .textScale(0.8F)
                .onPress(button -> mc.setScreenAndRender(new SearchScreen()))
                .onBuild(grid::addEntry)
                .build();

        DetailedButtonElement.create()
                .texture(bg)
                .icon(GuiTextures.DISCORD)
                .dimensions(w, h)
                .title("§eJoin the Discord!")
                .subtitle("§b§uJoin the others!")
                .textScale(0.8F)
                .onPress(btn -> {
                    try {
                        system.openUrl("https://discord.gg/tMaShNzNtP", starter + "Join the Discord! >>>", mc.currentScreen.getClass().newInstance());
                    }
                    catch (Exception ignore) {}
                })
                .onBuild(grid::addEntry)
                .build();

        DetailedButtonElement.create()
                .texture(bg)
                .icon(GuiTextures.CODE)
                .dimensions(w, h)
                .title("Source Code")
                .subtitle("§b§uRead our source!")
                .textScale(0.8F)
                .onPress(btn -> {
                    try {
                        system.openUrl("https://github.com/itzispyder/clickcrystals", starter + "Read our code!", mc.currentScreen.getClass().newInstance());
                    }
                    catch (Exception ignore) {}
                })
                .onBuild(grid::addEntry)
                .build();

        DetailedButtonElement.create()
                .texture(bg)
                .icon(GuiTextures.ICON)
                .dimensions(w, h)
                .title("Check Updates!")
                .subtitle("§b§uCheck for updates!")
                .textScale(0.8F)
                .onPress(btn -> {
                    try {
                        system.openUrl("https://modrinth.com/mod/clickcrystals", starter + "Go to Modrinth!", mc.currentScreen.getClass().newInstance());
                    }
                    catch (Exception ignore) {}
                })
                .onBuild(grid::addEntry)
                .build();

        DetailedButtonElement.create()
                .texture(bg)
                .icon(GuiTextures.ANNOUNCEMENT)
                .dimensions(w, h)
                .title("§eAnnouncements")
                .subtitle("§eRead me!!!")
                .textScale(0.8F)
                .onPress(btn -> mc.setScreenAndRender(new BulletinScreen()))
                .onBuild(grid::addEntry)
                .build();

        DetailedButtonElement.create()
                .texture(bg)
                .icon(GuiTextures.PEOPLE)
                .dimensions(w, h)
                .title("Credits")
                .subtitle("Your Idols Fr")
                .textScale(0.8F)
                .onPress(btn -> mc.setScreenAndRender(new CreditsScreen()))
                .onBuild(grid::addEntry)
                .build();

        grid.organize();
        grid.createPanel(this, (base.y + base.height) - grid.getStartY() - gap);
        grid.addAllToPanel();

        if (grid.hasPanel()) {
            this.addChild(grid.getPanel());
        }
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        ClickCrystalsBase.openClickCrystalsMenu();
    }
}
