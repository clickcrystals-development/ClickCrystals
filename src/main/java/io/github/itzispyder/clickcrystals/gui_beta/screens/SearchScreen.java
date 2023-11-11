package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui_beta.elements.client.ModuleElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.gui_beta.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchScreen extends DefaultBase {

    protected final AtomicInteger buttonTranslation = new AtomicInteger(-100);
    protected final SearchBarElement searchbar = new SearchBarElement(0, 0);
    private final GridOrganizer grid = new GridOrganizer(contentX, contentY + 21, contentWidth, 15, 1, 0);

    public SearchScreen() {
        super("Search Screen");
        this.navlistModules.forEach(this::removeChild);
        this.removeChild(buttonSearch);
        this.addChild(searchbar);
        system.scheduler.runRepeatingTask(() -> {
            buttonTranslation.getAndIncrement();
            buttonTranslation.getAndIncrement();
        }, 0, 1, 50);

        grid.createPanel(this, contentHeight - 21);
        this.addChild(grid.getPanel());
        this.filterByQuery(searchbar);
        this.selected = searchbar;
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderUtils.fillGradient(context, 0, 0, windowWidth, windowHeight, 0xE03873A9, 0xE0000000);

        context.getMatrices().push();
        context.getMatrices().translate(baseX, baseY, 0);

        // backdrop
        RoundRectBrush.drawRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Gray.BLACK);
        RoundRectBrush.drawTabTop(context, 110, 10, 300, 230, 10, Gray.DARK_GRAY);

        // navbar
        String text;
        int caret = 10;
        int translation = buttonTranslation.get();

        RenderUtils.drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        text = "ClickCrystals v%s".formatted(version);
        RenderUtils.drawText(context, text, 22, 11, 0.7F, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
        caret += 6;
        searchbar.x = baseX + 10;
        searchbar.y = baseY + caret - translation;
        caret += 16;
        buttonHome.x = baseX + 10;
        buttonHome.y = baseY + caret - translation;
        caret += 12;
        buttonModules.x = baseX + 10;
        buttonModules.y = baseY + caret - translation;
        caret += 12;
        buttonNews.x = baseX + 10;
        buttonNews.y = baseY + caret - translation;
        caret += 12;
        buttonSettings.x = baseX + 10;
        buttonSettings.y = baseY + caret - translation;

        context.getMatrices().pop();

        // content
        caret = contentY + 10;
        String query = searchbar.getQuery();
        if (query.isEmpty()) {
            text = "Search results for 'all'";
        } else {
            if (query.length() > 20) {
                query = query.substring(0, 20).concat("...");
            }
            text = "Search results for '%s'".formatted(query);
        }
        RenderUtils.drawText(context, text, contentX + 10, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
    }

    public void filterByQuery(SearchBarElement searchbar) {
        grid.clear();
        List<Module> list = system.collectModules().stream()
                .filter(m -> m.getSearchQuery().contains(searchbar.getLowercaseQuery()))
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        for (Module module : list) {
            ModuleElement e = new ModuleElement(module, 0, 0);
            grid.addEntry(e);
        }
        grid.organize();
        grid.clearPanel();
        grid.addAllToPanel();
    }

    public SearchBarElement getSearchbar() {
        return searchbar;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        filterByQuery(searchbar);
        return true;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new SearchScreen());
    }
}
