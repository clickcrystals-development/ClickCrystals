package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class DoubleSettingElement extends GuiElement {

    public static final int COLOR_FILL = 0xFF00A8f3;
    public static final int COLOR_EMPTY = 0xFF696969;
    private final ImageElement knob;
    private final TextElement titleText;
    private final DoubleSetting setting;

    public DoubleSettingElement(DoubleSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        this.knob = new ImageElement(GuiTextures.SETTING_NUMBER_KNOB, x, y, 10, 10);
        this.addChild(knob);

        double range = setting.getMax() - setting.getMin();
        double value = setting.getVal() - setting.getMin();
        double ratio = value / range;
        double ratioWidth = width * ratio;
        this.knob.setX((int)(x + ratioWidth));

        this.titleText = new TextElement(setting.getName() + ": §3" + setting.getVal(), TextAlignment.LEFT, 0.5F, x + 100, y);
        TextElement desc = new TextElement("§7" + setting.getDescription(), TextAlignment.LEFT, 0.45F, titleText.x, titleText.y + 5);
        this.addChild(titleText);
        this.addChild(desc);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (mc.currentScreen instanceof GuiScreen screen && screen.selected == knob) {
            knob.setX(MathUtils.minMax(mouseX, x, x + width));
        }

        DrawableUtils.drawHorizontalLine(context, x, y + 4, width + 10, 2, COLOR_EMPTY);
        DrawableUtils.drawHorizontalLine(context, x, y + 4, knob.x - x, 2, COLOR_FILL);
        this.titleText.setText(setting.getName() + ": §3" + setting.getVal());

        double range = setting.getMax() - setting.getMin();
        double ratio = (knob.x - x) / (double)width;
        double value = range * ratio;
        setting.setVal(value + setting.getMin());
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return rendering && mouseX > x && mouseX < x + width + 10 && mouseY > y && mouseY < y + height;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            screen.selected = knob;
        }
    }

    public DoubleSetting getSetting() {
        return setting;
    }
}
