package io.github.itzispyder.clickcrystals.gui.elements.overviewmode;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class ModuleElement extends GuiElement {

    private final Module module;

    public ModuleElement(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        Gray color = Gray.DARK_GRAY;

        if (module.isEnabled()) {
            color = Gray.GRAY;
        }
        if (isHovered(mouseX, mouseY)) {
            color = Gray.LIGHT_GRAY;
        }

        RoundRectBrush.drawRoundRect(context, x, y, width, height, 3, color);

        String text = (module.isEnabled() ? "§b" : "§7") + module.getNameLimited();
        RenderUtils.drawText(context, text, x + 5, y + height / 3, 0.8F, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            module.setEnabled(!module.isEnabled(), false);
        }
    }

    public Module getModule() {
        return module;
    }
}
