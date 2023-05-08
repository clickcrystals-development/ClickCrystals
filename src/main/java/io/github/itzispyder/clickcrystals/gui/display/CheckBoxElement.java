package io.github.itzispyder.clickcrystals.gui.display;

import io.github.itzispyder.clickcrystals.gui.DisplayableElement;
import io.github.itzispyder.clickcrystals.modules.settings.Setting;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.util.math.MatrixStack;

public class CheckBoxElement extends DisplayableElement {

    private Setting<Boolean> boolSetting;
    private String label;
    private boolean checked;

    public CheckBoxElement(int x, int y, String label) {
        super(x, y, 10, 10);
        this.label = label;
        this.setPressAction(button -> {
            setChecked(!isChecked());
            if (getBoolSetting() == null) return;
            getBoolSetting().setVal(isChecked());
        });
    }

    @Override
    public void render(MatrixStack matrices, double mouseX, double mouseY) {
        if (!isVisible()) return;

        this.setFillColor(checked ? 0xD0FFFFFF : 0xD0000000);
        super.render(matrices, mouseX, mouseY);

        DrawableUtils.drawText(matrices, label, getX() + getWidth() + 5, getY() + (int)(getHeight() * 0.25), true);
    }

    public String getLabel() {
        return label;
    }

    public Setting<Boolean> getBoolSetting() {
        return boolSetting;
    }

    public void setBoolSetting(Setting<Boolean> boolSetting) {
        this.boolSetting = boolSetting;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public boolean canDrag() {
        return false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
