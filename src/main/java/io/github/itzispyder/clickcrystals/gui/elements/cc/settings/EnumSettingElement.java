package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class EnumSettingElement extends GuiElement {

    private final EnumSetting<?> setting;

    public EnumSettingElement(EnumSetting<?> setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;

        ImageElement bg = new ImageElement(GuiTextures.SETTING_STRING, x, y, width, height);
        TextElement title = new TextElement(setting.getName(), TextAlignment.LEFT, 0.5F, x + 105, y);
        TextElement desc = new TextElement("§7" + setting.getDescription(), TextAlignment.LEFT, 0.45F, title.x, title.y + 5);
        this.addChild(title);
        this.addChild(desc);
        this.addChild(bg);

        AbstractElement reset = new AbstractElement(x + 92, y, height, height, (context, mouseX, mouseY, button) -> {
            context.drawTexture(GuiTextures.RESET, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
        }, (button) -> {
            setting.setVal(setting.getDef());
        });
        this.addChild(reset);
    }

    public EnumSetting<?> getSetting() {
        return setting;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int cX = x + width / 2;
        int cY = y + (int)(height * 0.33);
        String display = setting.getVal().name();

        RenderUtils.drawCenteredText(context, display, cX, cY, 0.6F, true);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        setting.next();
    }
}
