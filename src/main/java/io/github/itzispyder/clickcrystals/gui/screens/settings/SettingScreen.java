package io.github.itzispyder.clickcrystals.gui.screens.settings;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class SettingScreen extends DefaultBase {

    public SettingScreen() {
        super("Setting Screen");
        GridOrganizer grid = new GridOrganizer(contentX, contentY + 21, contentWidth, 15, 1, 0);

        grid.addEntry(new ScreenShortcut("Modules Configuration", "Browse ClickCrystals modules and features", new BrowsingScreen()));
        grid.addEntry(new ScreenShortcut("Keybindings Settings", "Edit and change keybindings for the client", new KeybindScreen()));
        grid.addEntry(new ScreenShortcut("Advanced", "More detailed settings for the client", new AdvancedSettingScreen()));

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
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
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


    private static class ScreenShortcut extends ModuleElement {
        private final Screen destination;
        private final String title, details;

        public ScreenShortcut(String title, String details, int x, int y, Screen destination) {
            super(null, x, y);
            super.setTooltip("§7Browsing shortcut");
            this.destination = destination;
            this.title = title;
            this.details = details;
        }

        public ScreenShortcut(String title, String details, Screen destination) {
            this(title, details, 0, 0, destination);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fill(context, x, y, width, height, 0x60FFFFFF);
            }
            RenderUtils.drawText(context, title, x + 10, y + height / 3, 0.7F, false);
            RenderUtils.drawText(context, "§7- " + details, x + 100, y + height / 3, 0.7F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            mc.setScreen(destination);
        }
    }
}
