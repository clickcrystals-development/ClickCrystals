package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ModuleWidget extends CCWidget {

    public static final int
            DEFAULT_WIDTH = 80,
            DEFAULT_HEIGHT = 12;

    public static boolean alreadyReading = false;
    private boolean readingDescription = false;
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
        DrawableUtils.drawCenteredText(matrices, module.getCurrentStateLabel(), getX() + (getWidth() / 2), (int)(getY() + (getHeight() * 0.33)), true);

        if (readingDescription) this.renderDescription(matrices);
    }

    public void renderDescription(MatrixStack matrices) {
        final Window win = mc.getWindow();
        final int width = win.getScaledWidth();
        final int height = win.getScaledHeight();
        final int left = width / 4;
        final int top = height / 4;

        DrawableHelper.fill(matrices, left, top, (int)(width * 0.75), (int)(height * 0.75), 0xD0000000);
        DrawableHelper.drawBorder(matrices, left, top, width / 2, height / 2, 0xFF1A9494);
        DrawableUtils.drawCenteredText(matrices, module.getName(), width / 2, top + 10, true);
        DrawableUtils.drawCenteredText(matrices, "§3Category: §b" + module.getCategory().name(), width / 2, top + 20, true);
        DrawableUtils.drawCenteredText(matrices, "§7" + module.getDescription(), width / 2, top + 40, true);

        matrices.translate(0.0F, 0.0F, 6942069420.0F);
    }

    public Module getModule() {
        return module;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;

        if (button == 0) {
            this.module.setEnabled(!module.isEnabled(), false);
            this.setMessage(Text.literal(this.module.getCurrentStateLabel()));
        }
        else if (button == 1) {
            if (alreadyReading) {
                alreadyReading = false;
                readingDescription = false;
                return false;
            }
            readingDescription = !readingDescription;
            alreadyReading = readingDescription;
        }

        return true;
    }
}
