package io.github.itzispyder.clickcrystals.gui.widgets;

import io.github.itzispyder.clickcrystals.gui.Draggable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

public abstract class CCWidget extends ClickableWidget implements Draggable {

    private boolean dragging;

    public CCWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
        this.dragging = false;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
    @Override
    public void playDownSound(SoundManager soundManager) {

    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public boolean canDrag() {
        return false;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
    }
}
