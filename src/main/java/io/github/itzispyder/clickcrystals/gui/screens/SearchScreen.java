package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.elements.cc.DetailedModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.cc.SearchBarElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class SearchScreen extends ClickCrystalsBase {

    public SearchBarElement searchbar = new SearchBarElement(nav.x + nav.width + 10, base.y + 10, 100, 0.8F);
    public List<DetailedModuleElement> matches;

    public SearchScreen() {
        super("ClickCrystals Modules Search Screen");
        this.matches = new ArrayList<>();
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
        updateFilteredModules();
        return true;
    }

    private void updateFilteredModules() {
        this.children.removeIf(matches::contains);
        this.matches.clear();

        List<Module> modules = system.modules().values().stream()
                .filter(module -> module.toString().contains(searchbar.getQuery()))
                .sorted(Comparator.comparing(Module::getId))
                .toList();

        int row, column;
        row = column = 0;

        for (Module module : modules) {
            if (row * column >= 3 * 4) break;

            DetailedModuleElement me = new DetailedModuleElement(module, 0, 0, 100);
            int x = nav.x + nav.width + 10 + (me.width + 5) * column;
            int y = base.y + 10 + (me.height + 3) * row;
            me.moveTo(x, y);

            matches.add(me);
            this.addChild(me);

            if (++column >= 3) {
                column = 0;
                row++;
            }
        }
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }
}
