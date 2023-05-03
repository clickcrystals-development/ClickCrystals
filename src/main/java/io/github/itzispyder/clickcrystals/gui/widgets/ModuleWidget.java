package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ModuleWidget extends CCWidget {

    private final Module module;

    public ModuleWidget(int x, int y, int width, int height, Module module) {
        super(x, y, width, height, Text.literal(module.getNameLimited()));
        this.module = module;
    }

    public ModuleWidget(Module module) {
        this(0, 0, 100, 15, module);
    }

    @Override
    public void onRender(MatrixStack matrices, int x, int y, float d) {
        DrawableHelper.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x40000000);
        DrawableHelper.drawCenteredTextWithShadow(matrices, mc.textRenderer, module.getCurrentStateLabel(), getX() + (getWidth() / 2), getY() + (getHeight() / 2), 0xFFFFFFFF);
    }

    public Module getModule() {
        return module;
    }
}
