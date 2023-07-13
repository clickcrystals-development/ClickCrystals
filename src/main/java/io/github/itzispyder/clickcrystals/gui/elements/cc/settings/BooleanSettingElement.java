package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class BooleanSettingElement extends GuiElement {

    private final BooleanSetting setting;

    public BooleanSettingElement(BooleanSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;

        TextElement title = new TextElement(setting.getName(), TextAlignment.LEFT, 0.5F, x + 100, y);
        TextElement desc = new TextElement("§7" + setting.getDescription(), TextAlignment.LEFT, 0.45F, title.x, title.y + 5);
        this.addChild(title);
        this.addChild(desc);
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
        mc.player.playSound(SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.MASTER, 0.8F, 2);
        setting.setVal(!setting.getVal());
    }

    public BooleanSetting getSetting() {
        return setting;
    }
}
