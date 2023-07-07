package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.PressAction;
import net.minecraft.util.Identifier;

public class DetailedButtonElement extends ImageBannerElement {

    private PressAction<DetailedButtonElement> pressAction;

    public DetailedButtonElement(Identifier texture, int x, int y, int width, int height, String title, String subtitle, float textScale, PressAction<DetailedButtonElement> pressAction) {
        super(texture, x, y, width, height, title, subtitle, textScale);
        this.pressAction = pressAction;
    }

    public DetailedButtonElement(Identifier texture, int x, int y, int width, int height, String title, String subtitle, float textScale) {
        this(texture, x, y, width, height, title, subtitle, textScale, button -> {});
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        pressAction.onPress(this);
    }

    public void setPressAction(PressAction<DetailedButtonElement> pressAction) {
        this.pressAction = pressAction;
    }

    public PressAction<DetailedButtonElement> getPressAction() {
        return pressAction;
    }
}
