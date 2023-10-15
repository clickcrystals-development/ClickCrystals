package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.cc.DetailedModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.cc.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui_beta.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;

public class SearchScreen extends ClickCrystalsBase {

    private final List<Module> modules = system.modules().values().stream().sorted(Comparator.comparing(Module::getId)).toList();
    public SearchBarElement searchbar = new SearchBarElement(nav.x + nav.width + 10, base.y + 10, 100, 0.8F);
    public GridOrganizer grid;

    public SearchScreen() {
        super("ClickCrystals Modules Search Screen");

        int x = nav.x + nav.width + 10;
        int y = searchbar.y + searchbar.height + 10;
        grid = new GridOrganizer(x, y, 90, 45, 3, 5);
    }

    @Override
    protected void init() {
        this.addChild(searchbar);
        this.updateFilteredModules();
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        super.baseRender(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (mc.currentScreen instanceof GuiScreen screen && screen.selected != searchbar && GLFW.glfwGetKeyName(keyCode, scanCode) != null) {
            screen.selected = searchbar;
            searchbar.onKey(keyCode, scanCode);
        }
        updateFilteredModules();
        return true;
    }

    private void updateFilteredModules() {
        grid.clear();
        base.removeChild(grid.getPanel());
        grid.destroyPanel();

        this.modules.stream()
                .filter(module -> module.getSearchQuery().contains(searchbar.getLowercaseQuery()))
                .forEach(module -> {
                    DetailedModuleElement me = new DetailedModuleElement(module, 0, 0, 90);
                    grid.addEntry(me);
                });

        grid.organize();
        grid.createPanel(this, base.y + base.height - 10 - grid.getStartY());
        grid.addAllToPanel();
        grid.setPanelParent(base);
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        ClickCrystalsBase.openClickCrystalsMenu();
    }
}
