package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import net.minecraft.client.gui.DrawContext;

public class TestScreen extends GuiScreen {

    public TestScreen() {
        super("Test Screen");
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        RoundRectBrush.drawRoundHoriLine(context, 0, 0, 200, 20, Gray.LIGHT_GRAY);
        RoundRectBrush.drawRoundHoriLine(context, 200, 0, 20, 20, Gray.DARK_GRAY);
        RoundRectBrush.drawRoundVertLine(context, 0, 20, 200, 20, Gray.LIGHT_GRAY);
        RoundRectBrush.drawRoundVertLine(context, 20, 20, 20, 20, Gray.GRAY);

        RoundRectBrush.drawRoundRect(context, 40, 20, 500, 250, 10, Gray.LIGHT_GRAY);
    }
}
