package io.github.itzispyder.clickcrystals.gui_beta.elements.client;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class ModuleElement extends GuiElement {

    private final Module module;

    public ModuleElement(Module module, int x, int y) {
        super(x, y, 300, 10);
        this.module = module;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            RenderUtils.fill(context, x, y, width, height, 0x60FFFFFF);
        }

        String text;

        text = "  %s".formatted(module.getOnOrOff());
        RenderUtils.drawText(context, text, x, y + height / 3, 0.7F, false);
        text = " %s".formatted(module.getNameLimited());
        RenderUtils.drawText(context, text, x + 20, y + height / 3, 0.7F, false);
        text = "§7- %s".formatted(module.getDescriptionLimited());
        RenderUtils.drawText(context, text, x + width / 3, y + height / 3, 0.7F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            module.setEnabled(!module.isEnabled(), false);
        }
        else if (button == 1) {

        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return rendering && mouseX > x && mouseX < x + (width - 6) && mouseY > y && mouseY < y + height;
    }

    public Module getModule() {
        return module;
    }
}
