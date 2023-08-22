package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class LoadingIconElement extends ImageElement {

    private int deg;
    private final DrawContext ctx;

    public LoadingIconElement(Identifier tex, int x, int y, int width, int height) {
        super(tex, x, y, width, height);
        deg = 0;
        ctx = RenderUtils.createContext();
    }

    public LoadingIconElement(Identifier tex, int frameX, int frameY, int frameWidth, int frameHeight, int sideLength) {
        this(tex, frameX + frameWidth / 2 - sideLength / 2, frameY + frameHeight / 2 - sideLength / 2, sideLength, sideLength);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        super.onRender(ctx, mouseX, mouseY);
    }

    @Override
    public void onTick() {
        if (canRender()) {
            MatrixStack m = ctx.getMatrices();
            m.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(deg += 3), x + width / 2.0F, y + height / 2.0F, 0.0F);
        }
     }
}
