package io.github.itzispyder.clickcrystals.gui.elements.overviewmode;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchCategoryElement extends GuiElement {

    private final SearchBarElement searchbar;

    public SearchCategoryElement(int x, int y, int width) {
        super(x, y, width, 40);
        this.setDraggable(true);
        this.searchbar = new SearchBarElement(x + 5, y + 25, width - 10);
        searchbar.keyPressCallbacks.add((key, click, scancode, modifiers) -> filterQuery(searchbar));
        this.addChild(searchbar);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.fillRoundRect(context, x, y, width, height, 5, Shades.TRANS_BLACK);
        RenderUtils.drawTexture(context, Tex.ICON, x + 5, y + 7, 10, 10);
        RenderUtils.drawText(context, "Overview GUI", x + 18, y + 9, 0.9F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
        if (mc.currentScreen instanceof OverviewScreen screen) {
            if (searchbar.isHovered((int)mouseX, (int)mouseY)) {
                screen.selected = searchbar;
            }
            screen.bringForward(this);
        }
    }

    public void filterQuery(SearchBarElement searchbar) {
        GridOrganizer grid = new GridOrganizer(x + 5, y + 40, width - 10, 10, 1, 2);
        AtomicInteger caret = new AtomicInteger(40);
        this.getChildren().removeIf(e -> e instanceof ModuleElement);

        if (searchbar.getQuery().trim().isEmpty()) {
            this.height = 45;
            return;
        }

        List<Module> list = system.collectModules().stream()
                .filter(m -> m.getSearchQuery().contains(searchbar.getLowercaseQuery()))
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        int i = 0;
        for (Module module : list) {
            if (i >= 12) {
                break;
            }

            ModuleElement e = new ModuleElement(module, 0, 0, width - 10, 10);
            caret.getAndAdd(grid.getCellHeight() + grid.getGap());
            grid.addEntry(e);
            this.addChild(e);
            i++;
        }

        this.height = caret.get() + 5;
        grid.organize();
    }

    public SearchBarElement getSearchbar() {
        return searchbar;
    }
}
