package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.screens.ModuleSettingsScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class DetailedModuleElement extends GuiElement {

    private final ImageElement highlight;
    private final Module module;

    public DetailedModuleElement(Module module, int x, int y, int width) {
        super(x, y, width, width / 2);
        this.module = module;

        WidgetElement bg = new WidgetElement(x, y, width, height);
        ImageElement hl = new ImageElement(GuiTextures.HOLLOW_HORIZONTAL_WIDGET, x, y, width, height);
        this.highlight = hl;
        this.addChild(bg);
        this.addChild(hl);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        String prefix = module.isEnabled() ? "§b" : "§c";
        DrawableUtils.drawText(context, prefix + module.getName(), x + 5, y + 5, 0.8F, true);
        int i = 0;
        for (String line : StringUtils.wrapLines(module.getDescription(), 20, true)) {
            DrawableUtils.drawText(context, "§7" + line, x + 5, y + 13 + (int)(i++ * 5.5), 0.55F, true);
        }

        highlight.rendering = isHovered(mouseX, mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            module.setEnabled(!module.isEnabled(), false);
        }
        else if (button == 1) {
            mc.currentScreen.close();
            mc.setScreenAndRender(new ModuleSettingsScreen(module));
        }
    }

    public Module getModule() {
        return module;
    }
}
