package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class HomeScreen extends GuiScreen {

    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    public final SearchBarElement searchBar = new SearchBarElement(0, 0, 300);

    public HomeScreen() {
        super("ClickCrystals Home Screen");
        this.addChild(searchBar);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderUtils.fillGradient(context, 0, 0, windowWidth, windowHeight, 0xE03873A9, 0xE0000000);

        // title card
        context.getMatrices().push();
        context.getMatrices().translate(baseX, baseY, 0);

        RoundRectBrush.drawRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Gray.DARK_GRAY);
        RoundRectBrush.drawTabBottom(context, 15, baseHeight / 2 - 10, baseWidth - 30, baseHeight / 2, 10, Gray.BLACK);
        RenderUtils.drawTexture(context, Tex.Backdrops.BACKDROP_0, 10, 10, baseWidth - 20, baseHeight / 2 + 40);

        context.getMatrices().pop();

        // content
        int caret = baseY + 50;
        RenderUtils.drawCenteredText(context, "§lClickCrystals", baseX + baseWidth / 2, caret, 2.0F, false);
        caret += 20;
        RenderUtils.drawCenteredText(context, "Crystal PvP Enhanced", baseX + baseWidth / 2, caret, 1.0F, false);
        caret += 30;
        searchBar.x = baseX + baseWidth / 2 - searchBar.width / 2;
        searchBar.y = caret;
    }
}
