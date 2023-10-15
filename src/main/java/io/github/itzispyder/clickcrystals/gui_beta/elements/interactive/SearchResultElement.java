package io.github.itzispyder.clickcrystals.gui_beta.elements.interactive;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.screens.ModuleEditScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class SearchResultElement extends GuiElement {

    private final SearchResultListElement parentList;
    private final Module module;

    public SearchResultElement(SearchResultListElement parentList, Module result, int x, int y) {
        super(x, y, parentList.width, 10);
        this.module = result;
        this.parentList = parentList;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (!parentList.rendering) {
            this.rendering = false;
            return;
        }
        else {
            this.rendering = true;
        }

        if (isHovered(mouseX, mouseY)) {
            RenderUtils.fill(context, x, y, width, height, 0x60FFFFFF);
        }

        String text;
        RenderUtils.drawTexture(context, module.getCategory().texture(), x + 5, y + 1, 8, 8);
        text = " §8|   §f%s".formatted(module.getNameLimited());
        RenderUtils.drawText(context, text, x + 25, y + height / 3, 0.7F, false);
        text = "§7- %s".formatted(module.getDescriptionLimited());
        RenderUtils.drawText(context, text, x + width / 3 + 5, y + height / 3, 0.7F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        mc.setScreen(new ModuleEditScreen(module));
    }

    public Module getModule() {
        return module;
    }
}
