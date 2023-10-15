package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui_beta.elements.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.settings.IntegerSetting;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class IntegerSettingElement extends GuiElement {

    public static final int COLOR_FILL = 0xFF00A8f3;
    public static final int COLOR_EMPTY = 0xFF696969;
    private final ImageElement knob;
    private final TextElement titleText;
    private final IntegerSetting setting;
    private boolean settingRenderUpdates;

    public IntegerSettingElement(IntegerSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.settingRenderUpdates = true;
        this.setting = setting;

        this.knob = new ImageElement(GuiTextures.SETTING_NUMBER_KNOB, x, y, 10, 10);
        this.titleText = new TextElement(setting.getName() + ": §3" + setting.getVal(), TextAlignment.LEFT, 0.5F, x + 105, y);
        TextElement desc = new TextElement("§7" + setting.getDescription(), TextAlignment.LEFT, 0.45F, titleText.x, titleText.y + 5);
        this.addChild(knob);
        this.addChild(titleText);
        this.addChild(desc);

        AbstractElement reset = new AbstractElement(x + 92, y, height, height, (context, mouseX, mouseY, button) -> {
            context.drawTexture(GuiTextures.RESET, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
        }, (button) -> {
            disableSettingRenderUpdates();
            setting.setVal(setting.getDef());
            enableSettingRenderUpdates();
        });
        this.addChild(reset);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        int x = this.x;
        int y = this.y;
        int w = this.width;
        int h = this.height;
        int len;

        if (mc.currentScreen instanceof GuiScreen screen && screen.selected == knob) {
            this.knob.setX(MathUtils.minMax(mouseX, x, x + w));
            int range = setting.getMax() - setting.getMin();
            double ratio = (double)(knob.x - x) / (double)w;
            int value = (int)(range * ratio);
            setting.setVal(value + setting.getMin());
        }

        int range = setting.getMax() - setting.getMin();
        int value = setting.getVal() - setting.getMin();
        double ratio = (double)value / (double)range;
        len = (int)(w * ratio);

        if (settingRenderUpdates) {
            setting.setVal((int)(range * ratio + setting.getMin()));
        }

        RenderUtils.drawHorizontalLine(context, x, y + 4, w + 10, 2, COLOR_EMPTY);
        RenderUtils.drawHorizontalLine(context, x, y + 4, len, 2, COLOR_FILL);
        this.knob.setX(x + len);
        this.titleText.setText(setting.getName() + ": §3" + setting.getVal());
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

    public void enableSettingRenderUpdates() {
        settingRenderUpdates = true;
    }

    public void disableSettingRenderUpdates() {
        settingRenderUpdates = false;
    }

    public IntegerSetting getSetting() {
        return setting;
    }
}
