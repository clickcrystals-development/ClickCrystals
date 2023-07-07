package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.gui.elements.ui.ImageBannerElement;

public class HomeScreen extends DefaultBase {

    public HomeScreen() {
        super("ClickCrystals Home Screen");
    }

    @Override
    protected void init() {
        ImageBannerElement banner = new ImageBannerElement(TexturesIdentifiers.SMOOTH_BANNER, base.x + 10, base.y + 10, base.width - 20, (int)(base.height * (90.0 / 512.0)), "ClickCrystals", "Your Ultimate CPvP Assistance");
        this.addChild(banner);
    }
}
