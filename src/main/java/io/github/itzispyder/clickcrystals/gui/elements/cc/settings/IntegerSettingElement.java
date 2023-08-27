package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class IntegerSettingElement extends GuiElement {

    public static final int COLOR_FILL = 0xFF00A8f3;
    public static final int COLOR_EMPTY = 0xFF696969;
    private final ImageElement knob;
    private final TextElement titleText;
    private final IntegerSetting setting;

    public IntegerSettingElement(IntegerSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        this.knob = new ImageElement(GuiTextures.SETTING_NUMBER_KNOB, x, y, 10, 10);
        this.addChild(knob);

        int range = setting.getMax() - setting.getMin();
        int value = setting.getVal() - setting.getMin();
        double ratio = (double)value / (double)range;
        int ratioWidth = (int)(width * ratio);
        this.knob.setX(x + ratioWidth);

        this.titleText = new TextElement(setting.getName() + ": §3" + setting.getVal(), TextAlignment.LEFT, 0.5F, x + 105, y);
        TextElement desc = new TextElement("§7" + setting.getDescription(), TextAlignment.LEFT, 0.45F, titleText.x, titleText.y + 5);
        this.addChild(titleText);
        this.addChild(desc);

        AbstractElement reset = new AbstractElement(x + 92, y, height, height, (context, mouseX, mouseY, button) -> {
            context.drawTexture(GuiTextures.RESET, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
        }, (button) -> {
            int ran = setting.getMax() - setting.getMin();
            int val = setting.getDef() - setting.getMin();
            double rat = (double)val / (double)ran;
            int ratWid = (int)(width * rat);
            knob.setX(x + ratWid);
        });
        this.addChild(reset);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (mc.currentScreen instanceof GuiScreen screen && screen.selected == knob) {
            knob.setX(MathUtils.minMax(mouseX, x, x + width));
        }

        RenderUtils.drawHorizontalLine(context, x, y + 4, width + 10, 2, COLOR_EMPTY);
        RenderUtils.drawHorizontalLine(context, x, y + 4, knob.x - x, 2, COLOR_FILL);
        this.titleText.setText(setting.getName() + ": §3" + setting.getVal());

        int range = setting.getMax() - setting.getMin();
        double ratio = (knob.x - x) / (double)width;
        int value = (int)(range * ratio);
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

    public IntegerSetting getSetting() {
        return setting;
    }
}
