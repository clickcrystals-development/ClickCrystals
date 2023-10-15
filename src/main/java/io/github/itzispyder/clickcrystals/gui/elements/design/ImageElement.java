package io.github.itzispyder.clickcrystals.gui.elements.design;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ImageElement extends GuiElement {

    private Identifier texture;

    public ImageElement(Identifier texture, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.texture = texture;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        context.drawTexture(texture, x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }

    public Identifier getTexture() {
        return texture;
    }
}
