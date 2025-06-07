package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.ModuleEditScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchResultsElement extends GuiElement {

    private final SearchBarElement searchbar;
    private final List<ResultElement> searchResults = new ArrayList<>();

    public SearchResultsElement(SearchBarElement searchBar) {
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

        RenderUtils.fillRoundTabBottom(context, x, y + h / 2, w, this.height, 5, Shades.LIGHT);
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

        List<Module> list = system.collectModules().stream()
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
            ResultElement sre = new ResultElement(list.get(i), margin, caret);
            searchResults.add(sre);
            this.addChild(sre);
            caret += sre.height;
        }

        this.setHeight(caret - searchbar.y - 3);
    }

    public SearchBarElement getSearchbar() {
        return searchbar;
    }

    private class ResultElement extends GuiElement {
        private final Module module;

        public ResultElement(Module result, int x, int y) {
            super(x, y, SearchResultsElement.this.width, 10);
            this.module = result;
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (!SearchResultsElement.this.rendering) {
                this.rendering = false;
                return;
            }
            else {
                this.rendering = true;
            }

            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x60B0B0B0);
            }

            String text;
            RenderUtils.drawTexture(context, module.getCategory().texture(), x + 5, y + 1, 8, 8);
            text = " §8|   %s".formatted(module.getNameLimited());
            RenderUtils.drawText(context, text, x + 25, y + height / 3, 0.7F, false);
            text = "§8- %s".formatted(module.getDescriptionLimited());
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
}
