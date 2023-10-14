package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.CategoryElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ModuleScreen extends GuiScreen {

    public static Category currentCategory = Categories.CRYSTALLING;
    private final List<CategoryElement> categoryElementList = new ArrayList<>();

    public ModuleScreen() {
        super("Test Screen");

        int caret = 40;
        for (Category category : Categories.getCategories().values()) {
            CategoryElement e = new CategoryElement(category, 10, caret);
            this.addChild(e);
            categoryElementList.add(e);
            caret += 10;
        }
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        int sw = context.getScaledWindowWidth();
        int sh = context.getScaledWindowHeight();
        int w = 420;
        int h = 240;
        int tx = (int)(sw / 2.0 - w / 2.0);
        int ty = (int)(sh / 2.0 - h / 2.0);

        // navbar width = 90
        // navbar height = 230

        RenderUtils.fillGradient(context, 0, 0, sw, sh, 0xE03873A9, 0xE0000000);

        context.getMatrices().push();
        context.getMatrices().translate(tx, ty, 0);

        // backdrop
        RoundRectBrush.drawRoundRect(context, 0, 0, w, h, 10, Gray.DARK_GRAY);
        RoundRectBrush.drawTabTop(context, 110, 10, 300, 230, 10, Gray.GRAY);

        // navbar
        String text;
        int caret = 10;

        RenderUtils.drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        text = "ClickCrystals v%s".formatted(version);
        RenderUtils.drawText(context, text, 22, 11, 0.7F, false);
        caret += 10;
        line(context, caret);
        caret += 10;
        text = "Modules (%s)".formatted(system.modules().size());
        RenderUtils.drawText(context, text, 10, caret, 0.65F, false);

        for (CategoryElement ce : categoryElementList) {
            caret += 10;
            ce.x = tx + 10;
            ce.y = ty + caret;
        }
        caret += 20;
        line(context, caret);

        // content

        context.getMatrices().pop();
    }

    private void line(DrawContext context, int caret) {
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
    }

    public void filterByCategory(Category category) {
        List<Module> list = system.modules().values().stream().filter(m -> m.getCategory() == category).toList();
        for (Module module : list) {

        }
    }
}
