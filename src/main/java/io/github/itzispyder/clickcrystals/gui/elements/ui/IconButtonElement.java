package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.elements.PressAction;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import net.minecraft.util.Identifier;

public class IconButtonElement extends ImageElement {

    private PressAction<IconButtonElement> pressAction;

    public IconButtonElement(Identifier texture, int x, int y, int width, int height, PressAction<IconButtonElement> pressAction) {
        super(texture, x, y, width, height);
        this.pressAction = pressAction;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        pressAction.onPress(this);
    }

    public PressAction<IconButtonElement> getPressAction() {
        return pressAction;
    }

    public void setPressAction(PressAction<IconButtonElement> pressAction) {
        this.pressAction = pressAction;
    }
}
