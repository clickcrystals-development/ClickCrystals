package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.CategoryElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

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

    public DefaultBase(String title) {
        super(title);
    }

    public void renderDefaultBase(DrawContext context) {
        RenderUtils.fillGradient(context, 0, 0, windowWidth, windowHeight, 0xE03873A9, 0xE0000000);

        context.getMatrices().push();
        context.getMatrices().translate(baseX, baseY, 0);

        // backdrop
        RoundRectBrush.drawRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Gray.DARK_GRAY);
        RoundRectBrush.drawTabTop(context, 110, 10, 300, 230, 10, Gray.GRAY);

        // navbar
        String text;
        int caret = 10;

        RenderUtils.drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        text = "ClickCrystals v%s".formatted(version);
        RenderUtils.drawText(context, text, 22, 11, 0.7F, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
        caret += 10;
        text = "Modules (%s)".formatted(system.modules().size());
        RenderUtils.drawText(context, text, 10, caret, 0.65F, false);

        for (CategoryElement ce : navlistModules) {
            caret += 10;
            ce.x = baseX + 10;
            ce.y = baseY + caret;
        }
        caret += 20;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);

        context.getMatrices().pop();
    }
}
