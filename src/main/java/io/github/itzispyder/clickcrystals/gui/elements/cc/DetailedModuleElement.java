package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalsBase;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.gui.screens.HomeScreen;
import io.github.itzispyder.clickcrystals.gui.screens.ModuleSettingsScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;

public class DetailedModuleElement extends GuiElement {

    private final Module module;

    public DetailedModuleElement(Module module, int x, int y, int width) {
        super(x, y, width, width / 2);
        this.module = module;

        WidgetElement bg = new WidgetElement(x, y, width, height);
        this.addChild(bg);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        String prefix = module.isEnabled() ? "§b" : "§c";
        RenderUtils.drawText(context, prefix + module.getName(), x + 5, y + 5, 0.8F, true);
        int i = 0;
        for (String line : StringUtils.wrapLines(module.getDescription(), 20, true)) {
            RenderUtils.drawText(context, "§7" + line, x + 5, y + 13 + (int)(i++ * 5.5), 0.55F, true);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            module.setEnabled(!module.isEnabled(), false);
        }
        else if (button == 1) {
            if (mc.currentScreen instanceof ClickCrystalsBase|| mc.currentScreen instanceof HomeScreen) {
                ClickCrystalsBase.setPrevOpened(((DefaultBase)mc.currentScreen).getClass());
                mc.setScreenAndRender(new ModuleSettingsScreen(module, true));
            }
        }
    }

    public Module getModule() {
        return module;
    }
}
