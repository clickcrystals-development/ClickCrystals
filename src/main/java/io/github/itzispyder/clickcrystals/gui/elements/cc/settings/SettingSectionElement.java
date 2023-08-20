package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.design.DividerElement;
import io.github.itzispyder.clickcrystals.modules.settings.*;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class SettingSectionElement extends GuiElement {

    private final SettingSection settingSection;
    private final float textScale;

    public SettingSectionElement(SettingSection settingSection, int x, int y, int width, int height, float textScale) {
        super(x, y, width, height);
        this.settingSection = settingSection;
        this.textScale = textScale;

        DividerElement title = new DividerElement(settingSection.getName(), x, y, width, 0, textScale);
        this.addChild(title);

        int nameHeight = (int)(10 * textScale);
        int caret = y + nameHeight + 2;

        for (ModuleSetting<?> setting : settingSection.getSettings()) {
            int elementWidth = 20;
            if (setting instanceof NumberSetting<?>) elementWidth = 80;
            else if (setting instanceof StringSetting || setting instanceof EnumSetting<?>) elementWidth = 90;

            GuiElement element = setting.toGuiElement(x, caret, elementWidth, 10);
            this.addChild(element);
            caret += element.height + 2;
        }
        this.height = caret - y;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            for (int i = getChildren().size() - 1; i >= 0; i--) {
                GuiElement child = getChildren().get(i);
                if (child.isHovered((int)mouseX, (int)mouseY)) {
                    screen.selected = child;
                    child.onClick(mouseX, mouseY, button);
                    break;
                }
            }
        }
    }

    public SettingSection getSettingGroup() {
        return settingSection;
    }

    public float getTextScale() {
        return textScale;
    }
}
