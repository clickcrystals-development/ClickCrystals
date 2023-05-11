package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.gui.Draggable;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalsModuleScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

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
        this.renderModule(matrices, mouseX, mouseY);
    }

    public void renderModule(MatrixStack matrices, int mouseX, int mouseY) {
        int fillColor = 0x40101010;
        if (this.module.isEnabled()) fillColor = 0x40805050;
        if (isMouseOver(mouseX, mouseY)) {
            if (mc.currentScreen instanceof ClickCrystalsModuleScreen screen && !(screen.isEditingModule && screen.descriptionWindow.isMouseOver(mouseX, mouseY))) {
                fillColor = 0x60909090;
            }
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

        if (mc.currentScreen instanceof ClickCrystalsModuleScreen screen) {
            if (screen.isEditingModule && screen.descriptionWindow.isMouseOver(mouseX, mouseY)) {
                return false;
            }

            if (button == 0) {
                if (screen.isEditingModule) {
                    screen.selectedModule = null;
                    screen.isEditingModule = false;
                    return true;
                }
                this.module.setEnabled(!module.isEnabled(), false);
                this.setMessage(Text.literal(this.module.getCurrentStateLabel()));
            }
            else if (button == 1) {
                screen.selectedModule = module;
                screen.isEditingModule = true;
                screen.descriptionWindow.setX((int)mouseX);
                screen.descriptionWindow.setY((int)mouseY);
            }
        }
        return true;
    }

    @Override
    public <T extends Draggable> List<T> getDraggableChildren() {
        return null;
    }
}
