package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.AbstractElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.CategoryElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultBase extends GuiScreen {

    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    public final int navWidth = 90;
    public final int navHeight = 230;
    public final int navX = baseX + 10;
    public final int navY = baseY + 10;
    public final int contentWidth = 300;
    public final int contentHeight = 230;
    public final int contentX = baseX + 110;
    public final int contentY = baseY + 10;
    public final List<CategoryElement> navlistModules = new ArrayList<>();
    protected final AbstractElement buttonSearch, buttonHome, buttonModules, buttonNews, buttonSettings;

    public DefaultBase(String title) {
        super(title);

        int caret = 40;
        for (Category category : Categories.getCategories().values()) {
            CategoryElement e = new CategoryElement(category, 10, caret);
            this.addChild(e);
            navlistModules.add(e);
            caret += 10;
        }

        buttonSearch = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("LEFT-CLICK to search")
                .onPress(button -> mc.setScreen(new SearchScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, 12, Gray.LIGHT);
                    if (button.isHovered(mouseX, mouseY)) {
                        RenderUtils.drawVerticalLine(context, button.x + 7, button.y + 2, 8, 1, 0xFF222222);
                    } else {
                        RenderUtils.drawText(context, "§7Search module i.e.", button.x + 7, button.y + button.height / 3, 0.7F, false);
                    }
                }).build();
        buttonHome = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("Back to Home")
                .onPress(button -> mc.setScreen(new HomeScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, 10, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawTexture(context, Tex.Icons.HOME, button.x + 2, button.y, button.height, button.height);
                    RenderUtils.drawText(context, "Home", button.x + button.height + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        buttonModules = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("Browse modules")
                .onPress(button -> mc.setScreen(new ModuleScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, 10, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawTexture(context, Tex.Icons.MODULES, button.x + 2, button.y, button.height, button.height);
                    RenderUtils.drawText(context, "Modules", button.x + button.height + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        buttonNews = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("View announcements")
                .onPress(button -> mc.setScreen(new BulletinScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, 10, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawTexture(context, Tex.Icons.ANNOUNCE, button.x + 2, button.y, button.height, button.height);
                    RenderUtils.drawText(context, "What's New?", button.x + button.height + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        buttonSettings = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("Browse settings")
                .onPress(button -> mc.setScreen(new SettingScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, 10, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawTexture(context, Tex.Icons.SETTINGS, button.x + 2, button.y, button.height, button.height);
                    RenderUtils.drawText(context, "Settings", button.x + button.height + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        this.addChild(buttonSearch);
        this.addChild(buttonHome);
        this.addChild(buttonModules);
        this.addChild(buttonNews);
        this.addChild(buttonSettings);
    }

    public void renderDefaultBase(MatrixStack context) {
        RenderUtils.fillGradient(context, 0, 0, windowWidth, windowHeight, 0xA03873A9, 0xA0000000);

        context.push();
        context.translate(baseX, baseY, 0);

        // backdrop
        RoundRectBrush.drawRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Gray.BLACK);
        RoundRectBrush.drawTabTop(context, 110, 10, 300, 230, 10, Gray.DARK_GRAY);

        // navbar
        String text;
        int caret = 10;

        RenderUtils.drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        text = "ClickCrystals v%s".formatted(version);
        RenderUtils.drawText(context, text, 22, 11, 0.7F, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
        caret += 10;
        text = "Modules (%s)".formatted(system.collectModules().size());
        RenderUtils.drawText(context, text, 10, caret, 0.65F, false);

        for (CategoryElement ce : navlistModules) {
            caret += 10;
            ce.x = baseX + 10;
            ce.y = baseY + caret;
        }
        caret += 20;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
        caret += 6;
        buttonSearch.x = baseX + 10;
        buttonSearch.y = baseY + caret;
        caret += 16;
        buttonHome.x = baseX + 10;
        buttonHome.y = baseY + caret;
        caret += 12;
        buttonModules.x = baseX + 10;
        buttonModules.y = baseY + caret;
        caret += 12;
        buttonNews.x = baseX + 10;
        buttonNews.y = baseY + caret;
        caret += 12;
        buttonSettings.x = baseX + 10;
        buttonSettings.y = baseY + caret;

        caret += 20;
        RoundRectBrush.drawTabTop(context, 10, caret, 90, baseHeight - caret, 5, Gray.DARK_GRAY);
        caret += 5;
        RenderUtils.drawText(context, "Client Owners: ", 15, caret, 0.65F, false);
        caret += 8;
        RenderUtils.drawText(context, "§bImproperIssues §8(owner), ", 15, caret, 0.65F, false);
        caret += 8;
        RenderUtils.drawText(context, "§bobvWolf §8(owner) ", 15, caret, 0.65F, false);

        context.pop();
    }
}
