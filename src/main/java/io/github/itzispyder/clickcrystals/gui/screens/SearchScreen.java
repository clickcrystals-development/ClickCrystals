package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.cc.DetailedModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.cc.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.AbstractElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchScreen extends ClickCrystalsBase {

    private final List<Module> modules = system.modules().values().stream().sorted(Comparator.comparing(Module::getId)).toList();
    public SearchBarElement searchbar = new SearchBarElement(nav.x + nav.width + 10, base.y + 10, 100, 0.8F);
    public List<DetailedModuleElement> matches;
    private static final int perPage = 9;
    private static final int minPage = 0;
    private static int maxPage = 0;
    private static int pageIndex = 0;

    public SearchScreen() {
        super("ClickCrystals Modules Search Screen");
        this.matches = new ArrayList<>();
        pageIndex = 0;
        maxPage = (int)Math.ceil(modules.size() / (double)perPage);

        AbstractElement pagePrev = AbstractElement.create()
                .pos(base.x + base.width - 70, searchbar.y)
                .dimensions(10, 12)
                .onPress(button -> pagePrev())
                .onRender((context, mouseX, mouseY, button) -> {
                    String display = pageIndex <= minPage ? "§8<" : "§f<";
                    DrawableUtils.drawCenteredText(context, display, button.x + button.width / 2, button.y + (int)(button.height * 0.33), 0.8F, true);
                })
                .build();

        AbstractElement pageText = AbstractElement.create()
                .pos(pagePrev.x + pagePrev.width + 2, searchbar.y)
                .dimensions(20, 12)
                .onRender((context, mouseX, mouseY, button) -> {
                    String display = (pageIndex + 1) + "/" + maxPage;
                    DrawableUtils.drawCenteredText(context, display, button.x + button.width / 2, button.y + (int)(button.height * 0.33), 0.8F, true);
                })
                .build();

        AbstractElement pageNext = AbstractElement.create()
                .pos(pageText.x + pageText.width + 2, searchbar.y)
                .dimensions(10, 12)
                .onPress(button -> pageNext())
                .onRender((context, mouseX, mouseY, button) -> {
                    String display = pageIndex + 1 >= maxPage ? "§8>" : "§f>";
                    DrawableUtils.drawCenteredText(context, display, button.x + button.width / 2, button.y + (int)(button.height * 0.33), 0.8F, true);
                })
                .build();

        this.addChild(pageNext);
        this.addChild(pageText);
        this.addChild(pagePrev);
    }

    private void validatePageIndex() {
        if (pageIndex >= maxPage) {
            pageIndex = maxPage - 1;
        }
        else if (pageIndex < minPage) {
            pageIndex = minPage;
        }
    }

    public void pageNext() {
        pageIndex++;
        validatePageIndex();
        this.updateFilteredModules();
    }

    public void pagePrev() {
        pageIndex--;
        validatePageIndex();
        this.updateFilteredModules();
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
        this.children.removeIf(matches::contains);
        this.matches.clear();

        List<Module> filtered = this.modules.stream()
                .filter(module -> module.getSearchQuery().contains(searchbar.getLowercaseQuery()))
                .toList();

        maxPage = (int)Math.ceil(filtered.size() / (double)perPage);
        validatePageIndex();

        int perRow = (int)Math.sqrt(perPage);
        int row, column;
        row = column = 0;

        for (int i = (pageIndex * perPage); i < filtered.size(); i++) {
            Module module;

            try {
                module = filtered.get(i);
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                break;
            }

            DetailedModuleElement me = new DetailedModuleElement(module, 0, 0, 90);
            int x = nav.x + nav.width + 10 + (me.width + 5) * column;
            int y = searchbar.y + searchbar.height + 10 + (me.height + 3) * row;

            me.moveTo(x, y);
            matches.add(me);
            this.addChild(me);

            if (++column >= perRow) {
                row++;
                if (row * column >= perPage) break;
                column = 0;
            }
        }
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
