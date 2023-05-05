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
}
