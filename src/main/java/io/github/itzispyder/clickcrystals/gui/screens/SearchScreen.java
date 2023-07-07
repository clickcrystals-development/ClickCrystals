package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.elements.cc.SearchBarElement;
import net.minecraft.client.gui.DrawContext;

public class SearchScreen extends ClickCrystalsBase {

    private int tickTimer;

    public SearchScreen() {
        super("ClickCrystals Modules Search Screen");
    }

    @Override
    protected void init() {
        SearchBarElement search = new SearchBarElement(nav.x + nav.width + 10, base.y + 10, 100, 0.8F);
        this.addChild(search);
    }

    @Override
    public void tick() {
        if (tickTimer++ >= 20) {

            tickTimer = 0;
        }
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        super.baseRender(context, mouseX, mouseY, delta);

    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }
}
