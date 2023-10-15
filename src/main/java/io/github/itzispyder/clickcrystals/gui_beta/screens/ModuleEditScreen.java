package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui.elements.design.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.module.SettingSectionElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class ModuleEditScreen extends DefaultBase {

    private final Module module;

    public ModuleEditScreen(Module module) {
        super("Editing Module " + module.getName());
        this.module = module;

        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);
        int caret = contentY + 25;

        for (SettingSection section : module.getData().getSettingSections()) {
            if (section.getSettings().isEmpty()) {
                continue;
            }
            SettingSectionElement sse = new SettingSectionElement(section, contentX + 5, caret);
            panel.addChild(sse);
            caret += sse.height + 2;
        }
        this.addChild(panel);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // content
        String text;
        int caret = contentY + 10;

        RenderUtils.drawTexture(context, module.getCategory().texture(), contentX + 10, caret - 7, 15, 15);
        text = "§7" + module.getCategory().name() + " \\ §f" + module.getName();
        RenderUtils.drawText(context, text, contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.DARK_GRAY.argb);
    }
}
