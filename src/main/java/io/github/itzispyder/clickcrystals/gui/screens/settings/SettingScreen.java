package io.github.itzispyder.clickcrystals.gui.screens.settings;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.gui.screens.HudEditScreen;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.gui.screens.profiles.ProfilesScreen;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.BooleanSupplier;

public class SettingScreen extends DefaultBase {

    public static final BooleanSupplier REQUIRE_IN_GAME = PlayerUtils::valid;
    public static final BooleanSupplier TRUE = () -> true;

    public SettingScreen() {
        super("Setting Screen");
        GridOrganizer grid = new GridOrganizer(contentX, contentY + 21, contentWidth, 15, 1, 0);

        grid.addEntry(new ScreenShortcut("Configuration Profiles", "Switch between profiles via GUI", 0, 0, new ProfilesScreen(), TRUE));
        grid.addEntry(new ScreenShortcut("Modules Configuration", "Browse ClickCrystals modules and features", 0, 0, new BrowsingScreen(), TRUE));
        grid.addEntry(new ScreenShortcut("HUD Position Config", "Edit and move HUDs around", 0, 0, new HudEditScreen(), REQUIRE_IN_GAME));
        grid.addEntry(new ScreenShortcut("Keybindings Settings", "Edit and change keybindings for the client", 0, 0, new KeybindScreen(), TRUE));
        grid.addEntry(new ScreenShortcut("Advanced", "More detailed settings for the client", 0, 0, new AdvancedSettingScreen(), TRUE));
        grid.addEntry(new ScreenShortcut("Client Information", "...", 0, 0, new InfoScreen(), TRUE));
        grid.addEntry(new FileShortcut("ClickCrystals Folder", "CC config, scripts, etc...", 0, 0, Config.PATH, TRUE));
        grid.addEntry(new FileShortcut(".Minecraft Folder", "MC assets", 0, 0, "", TRUE));
        grid.addEntry(new URLShortcut("Support", "ClickCrystals user support", 0, 0, "https://discord.gg/tMaShNzNtP", TRUE));

        grid.organize();
        grid.createPanel(this, contentHeight - 21);
        grid.addAllToPanel();
        this.addChild(grid.getPanel());
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawTexture(context, Tex.Icons.SETTINGS, contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, "Client Settings", contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new SettingScreen());
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.save();
        return true;
    }


    public static class ShortCut extends ModuleElement {
        private final Runnable destination;
        private final String title, details;
        private final BooleanSupplier check;

        public ShortCut(String title, String details, int x, int y, Runnable destination, BooleanSupplier check) {
            super(null, x, y);
            super.setTooltip(check.getAsBoolean() ? "§7Browsing shortcut" : "§cUnavailable, §cmost §clikely §cneed §cto §cbe §cin §cgame!");
            this.destination = destination;
            this.title = title;
            this.details = details;
            this.check = check;
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY) && check.getAsBoolean()) {
                RenderUtils.fillRect(context, x, y, width, height, 0x60FFFFFF);
            }
            RenderUtils.drawText(context, check.getAsBoolean() ? title : "§7" + title, x + 10, y + height / 3, 0.7F, false);
            RenderUtils.drawText(context, "§7- " + details, x + 100, y + height / 3, 0.7F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            if (check.getAsBoolean()) {
                destination.run();
            }
        }
    }

    public static class ScreenShortcut extends ShortCut {
        public ScreenShortcut(String title, String details, int x, int y, Screen destination, BooleanSupplier check) {
            super(title, details, x, y, () -> mc.execute(() -> mc.setScreen(destination)), check);
        }
    }

    public static class FileShortcut extends ShortCut {
        public FileShortcut(String title, String details, int x, int y, String filePath, BooleanSupplier check) {
            super(title, details, x, y, () -> system.openFile(filePath), check);
        }
    }

    public static class URLShortcut extends ShortCut {
        public URLShortcut(String title, String details, int x, int y, String url, BooleanSupplier check) {
            super(title, details, x, y, () -> system.openUrl(url), check);
        }
    }
}
