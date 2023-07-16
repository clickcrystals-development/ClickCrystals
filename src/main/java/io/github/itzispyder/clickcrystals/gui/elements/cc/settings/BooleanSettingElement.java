package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BooleanSettingElement extends GuiElement {

    private final BooleanSetting setting;

    public BooleanSettingElement(BooleanSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;

        TextElement title = new TextElement(setting.getName(), TextAlignment.LEFT, 0.5F, x + 105, y);
        TextElement desc = new TextElement("§7" + setting.getDescription(), TextAlignment.LEFT, 0.45F, title.x, title.y + 5);
        this.addChild(title);
        this.addChild(desc);

        AbstractElement reset = new AbstractElement(x + 92, y, height, height, (context, mouseX, mouseY, button) -> {
            context.drawTexture(GuiTextures.RESET, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
        }, (button) -> {
            setting.setVal(setting.getDef());
        });
        this.addChild(reset);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        Identifier texture;
        if (setting == null) {
            texture = GuiTextures.SETTING_BOOLEAN_NEUTRAL;
        }
        else if (setting.getVal()) {
            texture = GuiTextures.SETTING_BOOLEAN_ON;
        }
        else {
            texture = GuiTextures.SETTING_BOOLEAN_OFF;
        }

        context.drawTexture(texture, x, y, 0, 0, width, height, width, height);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        setting.setVal(!setting.getVal());
    }

    public BooleanSetting getSetting() {
        return setting;
    }
}
