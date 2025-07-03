package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.CategoryElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.screens.settings.SettingScreen;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultBase extends AnimatedBase {

    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    public final int navWidth = 90;
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
                    RenderUtils.fillRoundHoriLine(context, button.x, button.y, navWidth, 12, Shades.LIGHT);
                    RenderUtils.drawText(context, "§7Search module i.e.", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        buttonHome = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("Back to Home")
                .onPress(button -> mc.setScreen(new HomeScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RenderUtils.fillRoundHoriLine(context, button.x, button.y, navWidth, 10, Shades.LIGHT_GRAY);
                    }
                    RenderUtils.drawTexture(context, Tex.Icons.HOME, button.x + 2, button.y, button.height, button.height);
                    RenderUtils.drawText(context, "Home", button.x + button.height + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        buttonModules = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("Browse modules")
                .onPress(button -> UserInputListener.openModulesScreen())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RenderUtils.fillRoundHoriLine(context, button.x, button.y, navWidth, 10, Shades.LIGHT_GRAY);
                    }
                    RenderUtils.drawTexture(context, Tex.Icons.MODULES, button.x + 2, button.y, button.height, button.height);
                    RenderUtils.drawText(context, "Modules", button.x + button.height + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        buttonNews = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("View announcements")
                .onPress(button -> mc.setScreen(new BulletinScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RenderUtils.fillRoundHoriLine(context, button.x, button.y, navWidth, 10, Shades.LIGHT_GRAY);
                    }
                    RenderUtils.drawTexture(context, Tex.Icons.ANNOUNCE, button.x + 2, button.y, button.height, button.height);
                    RenderUtils.drawText(context, "What's New?", button.x + button.height + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        buttonSettings = AbstractElement.create().dimensions(navWidth, 10)
                .tooltip("Browse settings")
                .onPress(button -> mc.setScreen(new SettingScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RenderUtils.fillRoundHoriLine(context, button.x, button.y, navWidth, 10, Shades.LIGHT_GRAY);
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

    public void renderDefaultBase(DrawContext context) {
        renderOpaqueBackground(context);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(baseX, baseY);

        // backdrop
        RenderUtils.fillRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Shades.TRANS_BLACK);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 1, 0xFF00B7FF, 0xFF00B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, -10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundTabTop(context, 110, 10, 300, 230, 10, Shades.TRANS_DARK_GRAY);

        // navbar
        String text;
        int caret = 10;

        RenderUtils.drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        text = "ClickCrystals v%s".formatted(version);
        RenderUtils.drawText(context, text, 22, 11, 0.7F, false);
        caret += 10;
        RenderUtils.drawHorLine(context, 10, caret, 90, Shades.GRAY);
        caret += 10;
        text = "Modules (%s)".formatted(system.collectModules().size());
        RenderUtils.drawText(context, text, 10, caret, 0.65F, false);

        for (CategoryElement ce : navlistModules) {
            caret += 10;
            ce.x = baseX + 10;
            ce.y = baseY + caret;
        }
        caret += 20;
        RenderUtils.drawHorLine(context, 10, caret, 90, Shades.GRAY);
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
        RenderUtils.fillRoundTabTop(context, 10, caret, 90, baseHeight - caret, 5, Shades.TRANS_BLACK);
        caret += 5;
        RenderUtils.drawText(context, "Client Owners: ", 15, caret, 0.65F, false);
        caret += 8;
        RenderUtils.drawText(context, "§bImproperIssues §8(owner), ", 15, caret, 0.65F, false);
        caret += 8;
        RenderUtils.drawText(context, "§bobvWolf §8(owner), ", 15, caret, 0.65F, false);
        caret += 8;
        RenderUtils.drawText(context, "§bI-No-oNe §8(dev) ", 15, caret, 0.65F, false);

        context.getMatrices().popMatrix();
    }
}
