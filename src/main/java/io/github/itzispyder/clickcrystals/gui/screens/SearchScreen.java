package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

public class SearchScreen extends DefaultBase {

    protected final Animator animator = new Animator(200);
    protected final SearchBarElement searchbar = new SearchBarElement(0, 0);
    private final GridOrganizer grid = new GridOrganizer(contentX, contentY + 21, contentWidth, 15, 1, 0);

    public SearchScreen() {
        super("Search Screen");
        this.navlistModules.forEach(this::removeChild);
        this.removeChild(buttonSearch);
        this.addChild(searchbar);

        grid.createPanel(this, contentHeight - 21);
        this.addChild(grid.getPanel());
        this.filterByQuery(searchbar);
        this.selected = searchbar;
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderDefaultBase(context);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(baseX, baseY);

        // backdrop
        RenderUtils.fillRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Shades.TRANS_BLACK);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 1, 0xFF00B7FF, 0xFF00B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, -10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundTabTop(context, 110, 10, 300, 230, 10, Shades.TRANS_DARK_GRAY);

        // navbar
        String text;
        int caret = 10;
        int translation = (int)(-100 * animator.getProgressClampedReversed());

        RenderUtils.drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        text = "ClickCrystals v%s".formatted(version);
        RenderUtils.drawText(context, text, 22, 11, 0.7F, false);
        caret += 10;
        RenderUtils.drawHorLine(context, 10, caret, 90, Shades.GRAY);
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

        context.getMatrices().popMatrix();

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
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
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
