package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.ClickType;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.Typeable;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.callbacks.KeyPressCallback;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SearchBarElement extends GuiElement implements Typeable {

    public final List<KeyPressCallback> keyPressCallbacks = new ArrayList<>();
    private String query, defaultText;
    private boolean selectionBlinking;
    private int selectionBlink;

    public SearchBarElement(int x, int y, int width) {
        super(x, y, width, 12);
        this.query = "";
        this.defaultText = "§7Search module i.e.";
    }

    public SearchBarElement(int x, int y) {
        this(x, y, 90);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.fillRoundHoriLine(context, x, y, width, height, Shades.LIGHT);

        if (!(mc.currentScreen instanceof GuiScreen screen))
            return;
        if (screen.selected == this)
            RenderUtils.fillRoundShadow(context, x, y, width, height, height / 2, 3, 0x80FFFFFF, 0x00FFFFFF);

        String text = query;
        while (!text.isEmpty() && mc.textRenderer.getWidth(text) * 0.7F > width - height - 4) {
            text = text.substring(1);
        }

        if (screen.selected == this && !text.isEmpty())
            RenderUtils.drawText(context, "§0" + text, x + height / 2 + 2, y + height / 3, 0.7F, false);
        else if (!text.isEmpty())
            RenderUtils.drawText(context, "§7" + text, x + height / 2 + 2, y + height / 3, 0.7F, false);
        else
            RenderUtils.drawText(context, getDefaultText(), x + height / 2 + 2, y + height / 3, 0.7F, false);

        if (selectionBlinking) {
            int tx = (int)(x + height / 2 + 1 + mc.textRenderer.getWidth(text) * 0.7F);
            int ty = y + 2;
            RenderUtils.drawVerLine(context, tx, ty, height - 4, 0xE0000000);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    @Override
    public void onKey(int key, int scancode) {
        Typeable.super.onKey(key, scancode);
        for (KeyPressCallback callback : keyPressCallbacks) {
            callback.handleKey(key, ClickType.CLICK, scancode, -1);
        }
    }

    @Override
    public void onInput(Function<String, String> factory) {
        query = factory.apply(query);
    }

    @Override
    public void onTick() {
        super.onTick();

        if (mc.currentScreen instanceof GuiScreen screen) {
            if (screen.selected != this) {
                selectionBlinking = false;
                return;
            }

            if (selectionBlink++ >= 20) {
                selectionBlink = 0;
            }
            if (selectionBlink % 10 == 0 && selectionBlink > 0) {
                selectionBlinking = !selectionBlinking;
            }
        }
    }

    public String getQuery() {
        return query;
    }

    public String getLowercaseQuery() {
        return query.toLowerCase();
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }
}
