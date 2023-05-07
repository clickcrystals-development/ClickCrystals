package io.github.itzispyder.clickcrystals.gui.widgets;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

public abstract class CCWidget extends ClickableWidget {

    public CCWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
    @Override
    public void playDownSound(SoundManager soundManager) {

    }

    public void move(int deltaX, int deltaY) {
        this.setX(getX() + deltaX);
        this.setY(getY() + deltaY);
    }

    public void moveTo(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public void move(double deltaX, double deltaY) {
        this.setX((int)(getX() + deltaX));
        this.setY((int)(getY() + deltaY));
    }

    public void moveTo(double x, double y) {
        this.moveTo((int)x, (int)y);
    }
}
