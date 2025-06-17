package io.github.itzispyder.clickcrystals.gui.screens.modulescreen;

import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

public class BrowsingScreen extends DefaultBase {

    public static Category currentCategory = Categories.MISC;
    protected final GridOrganizer grid = new GridOrganizer(contentX, contentY + 21, contentWidth, 15, 1, 0);

    public BrowsingScreen() {
        super("Browsing Module Screen");

        grid.createPanel(this, contentHeight - 21);
        this.addChild(grid.getPanel());
        this.filterByCategory(currentCategory);
    }

    @Override
    protected void init() {
        super.init();
        if (!(this instanceof ScriptsBrowsingScreen) && currentCategory == Categories.SCRIPTED)
            mc.execute(() -> mc.setScreen(new ScriptsBrowsingScreen()));
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawTexture(context, currentCategory.texture(), contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, currentCategory.name(), contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
    }

    public void filterByCategory(Category category) {
        grid.clearPanel();
        grid.clear();
        List<Module> list = system.collectModules().stream()
                .filter(m -> m.getCategory() == category)
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        for (Module module : list) {
            ModuleElement e = new ModuleElement(module, 0, 0);
            grid.addEntry(e);
        }
        grid.organize();
        grid.addAllToPanel();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new BrowsingScreen());
    }
}
