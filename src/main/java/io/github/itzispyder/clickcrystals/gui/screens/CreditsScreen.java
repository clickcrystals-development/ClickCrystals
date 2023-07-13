package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.ui.ImageBannerElement;
import net.minecraft.util.Identifier;

public class CreditsScreen extends ClickCrystalsBase {

    public CreditsScreen() {
        super("ClickCrystals Credits Screen");
    }

    @Override
    protected void init() {
        ImageElement improperIssues = new ImageElement(GuiTextures.IMPROPERISSUES, nav.x + nav.width + 10, base.y + 10, 120, nav.height);
        ImageElement obvWolf = new ImageElement(GuiTextures.OBVWOLF, improperIssues.x + improperIssues.width + 90, nav.y + (int)(nav.height * 0.18), 75, (int)(nav.height * 0.80));
        this.addChild(improperIssues);
        this.addChild(obvWolf);

        Identifier descriptionBg = GuiTextures.SMOOTH_HORIZONTAL_WIDGET;
        ImageBannerElement improperDescription = new ImageBannerElement(descriptionBg, improperIssues.x + improperIssues.width / 2 + 20, improperIssues.y + (int)(improperIssues.height * 0.66), 120, 40, "ImproperIssues", "Owner and Head Dev of ClickCrystals", 1.0F);
        ImageBannerElement obvDescription = new ImageBannerElement(descriptionBg, obvWolf.x - 90, obvWolf.y + 10, 90, 40, "obvWolf", "CoOwner and Dev of ClickCrystals", 1.0F);
        ImageBannerElement obvDescription2 = new ImageBannerElement(descriptionBg, obvDescription.x, obvDescription.y + obvDescription.height + 2, 75, 12, "Server owner of ogre.fun", "", 0.5F);
        obvDescription2.setX(obvDescription2.x + obvDescription.width - obvDescription2.width);
        this.addChild(improperDescription);
        this.addChild(obvDescription);
        this.addChild(obvDescription2);
    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }
}
