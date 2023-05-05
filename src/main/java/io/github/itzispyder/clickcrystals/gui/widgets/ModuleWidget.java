package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ModuleWidget extends CCWidget {

    public static final int
            DEFAULT_WIDTH = 80,
            DEFAULT_HEIGHT = 12;

    private final Module module;

    public ModuleWidget(int x, int y, int width, int height, Module module) {
        super(x, y, width, height, Text.literal(module.getNameLimited()));
        this.module = module;
    }

    public ModuleWidget(Module module) {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, module);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int fillColor = 0x40101010;
        if (this.module.isEnabled()) fillColor = 0x40805050;
        if (isMouseOver(mouseX, mouseY)) fillColor = 0x40909090;

        DrawableHelper.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), fillColor);
        DrawableHelper.drawCenteredTextWithShadow(matrices, mc.textRenderer, module.getCurrentStateLabel(), getX() + (getWidth() / 2), getY() + (int)(getHeight() * 0.33), 0xFFFFFFFF);
    }

    public Module getModule() {
        return module;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;

        this.module.setEnabled(!module.isEnabled(), false);
        this.setMessage(Text.literal(this.module.getCurrentStateLabel()));
        return true;
    }
}
