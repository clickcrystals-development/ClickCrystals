package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalsModuleScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ModuleWidget extends CCWidget {

    private static final ClickCrystalsModuleScreen screen = ClickCrystals.CC_MODULE_SCREEN;

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
        this.renderModule(matrices, mouseX, mouseY);
    }

    public void renderModule(MatrixStack matrices, int mouseX, int mouseY) {
        int fillColor = 0x40101010;
        if (this.module.isEnabled()) fillColor = 0x40805050;
        if (isMouseOver(mouseX, mouseY) && !(screen.isEditingModule && screen.descriptionWindow.isMouseOver(mouseX, mouseY))) {
            fillColor = 0x40909090;
        }

        DrawableHelper.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), fillColor);
        DrawableUtils.drawCenteredText(matrices, module.getCurrentStateLabel(), getX() + (getWidth() / 2), (int)(getY() + (getHeight() * 0.33)), true);
    }

    public Module getModule() {
        return module;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        if (screen.isEditingModule) {
            if (!screen.descriptionWindow.isMouseOver(mouseX, mouseY)) {
                screen.selectedModule = null;
                screen.isEditingModule = false;
            }
            return false;
        }

        if (button == 0) {
            this.module.setEnabled(!module.isEnabled(), false);
            this.setMessage(Text.literal(this.module.getCurrentStateLabel()));
        }
        else if (button == 1) {
            screen.selectedModule = module;
            screen.isEditingModule = true;
            screen.descriptionWindow.setX((int)mouseX);
            screen.descriptionWindow.setY((int)mouseY);
        }

        return true;
    }
}
