package io.github.itzispyder.clickcrystals.gui.elements.overviewmode;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class ModuleEditElement extends GuiElement {

    private final Module module;

    public ModuleEditElement(GuiScreen parentScreen, Module module, int x, int y) {
        super(x, y, 300, 230);
        this.setDraggable(true);
        this.module = module;

        ScrollPanelElement panel = new ScrollPanelElement(parentScreen, x + 5, y + 21, width - 5, height - 21);
        int caret = y + 25;

        for (SettingSection section : module.getData().getSettingSections()) {
            if (section.getSettings().isEmpty()) {
                continue;
            }
            SettingSectionElement sse = new SettingSectionElement(section, x + 5, caret);
            panel.addChild(sse);
            caret += sse.height + 5;
        }
        this.addChild(panel);
        this.height = Math.min(230, caret - y);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.fillRoundRect(context, x, y, width, height, 5, Shades.TRANS_DARK_GRAY);

        int caret = y + 10;
        RenderUtils.drawTexture(context, module.getCategory().texture(), x + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, module.getName(), x + 30, caret - 4, false);
        RenderUtils.drawText(context, isHoverExit(mouseX, mouseY) ? "§bx" : "§7x", x + width - 15, caret - 4, 1.2F, false);
        caret += 10;
        RenderUtils.drawHorLine(context, x, caret, 300, Shades.BLACK);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (isHoverExit((int)mouseX, (int)mouseY) && mc.currentScreen instanceof OverviewScreen screen) {
            screen.removeCurrentEditing();
            return;
        }
        super.onClick(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int button) {
        super.onRelease(mouseX, mouseY, button);
        ClickCrystals.config.saveModule(module);
        ClickCrystals.config.save();
    }

    private boolean isHoverExit(int mouseX, int mouseY) {
        return mouseX > x + width - 20 && mouseX < x + width && mouseY > y && mouseY < y + 20;
    }

    public Module getModule() {
        return module;
    }
}
