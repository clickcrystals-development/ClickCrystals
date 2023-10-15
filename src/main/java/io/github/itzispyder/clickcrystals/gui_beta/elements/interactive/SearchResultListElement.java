package io.github.itzispyder.clickcrystals.gui_beta.elements.interactive;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchResultListElement extends GuiElement {

    private final SearchBarElement searchbar;
    private final List<SearchResultElement> searchResults = new ArrayList<>();

    public SearchResultListElement(SearchBarElement searchBar) {
        super(searchBar.x, searchBar.y, searchBar.width, searchBar.height);
        this.searchbar = searchBar;

        searchbar.keyPressCallbacks.add((key, click, scancode, modifiers) -> {
            this.updateResults();
        });
        this.updateResults();
        this.setRendering(false);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (mc.currentScreen instanceof GuiScreen screen && screen.selected != searchbar) {
            this.setRendering(false);
            return;
        }
        else {
            this.setRendering(true);
        }

        int x = searchbar.x;
        int y = searchbar.y;
        int w = searchbar.width;
        int h = searchbar.height;

        RoundRectBrush.drawTabBottom(context, x, y + h / 2, w, this.height, 5, Gray.GRAY);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public void updateResults() {
        this.getChildren().removeIf(searchResults::contains);
        this.searchResults.clear();
        this.setDimensions(searchbar.getDimensions());

        if (mc.currentScreen instanceof GuiScreen screen) {
            this.setRendering(screen.selected == searchbar);
        }

        List<Module> list = system.modules().values().stream()
                .filter(m -> m.getSearchQuery().contains(searchbar.getLowercaseQuery()))
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        if (list.isEmpty()) {
            return;
        }

        int max = Math.min(list.size(), 10);
        int caret = searchbar.y + searchbar.height + 2;
        int margin = searchbar.x;

        for (int i = 0; i < max; i++) {
            SearchResultElement sre = new SearchResultElement(this, list.get(i), margin, caret);
            searchResults.add(sre);
            this.addChild(sre);
            caret += sre.height;
        }

        this.setHeight(caret - searchbar.y - 3);
    }

    public SearchBarElement getSearchbar() {
        return searchbar;
    }
}
