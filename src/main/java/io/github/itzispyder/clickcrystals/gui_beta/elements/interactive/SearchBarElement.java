package io.github.itzispyder.clickcrystals.gui_beta.elements.interactive;

import io.github.itzispyder.clickcrystals.gui_beta.ClickType;
import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.Typeable;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.gui_beta.misc.callbacks.KeyPressCallback;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SearchBarElement extends GuiElement implements Typeable {

    public final List<KeyPressCallback> keyPressCallbacks = new ArrayList<>();
    private String query, defaultText;

    public SearchBarElement(int x, int y, int width) {
        super(x, y, width, 12);
        this.query = "";
        this.defaultText = "§7Search module i.e.";
    }

    public SearchBarElement(int x, int y) {
        this(x, y, 90);
    }

    @Override
    public void onRender(MatrixStack context, int mouseX, int mouseY) {
        RoundRectBrush.drawRoundHoriLine(context, x, y, width, height, Gray.LIGHT);
        if (mc.currentScreen instanceof GuiScreen screen) {
            String text = query;

            if (screen.selected == this) {
                if (text.length() == 0) {
                    RenderUtils.drawText(context, "§8§0§l︳", x + height / 2 + 2, y + height / 3, 0.7F, false);
                } else {
                    while (text.length() > 0 && mc.textRenderer.getWidth(text) * 0.7F > width - height - 4) {
                        text = text.substring(1);
                    }
                    String displayText = screen.selected == this ? "§8%s§0§l︳".formatted(text) : text;
                    RenderUtils.drawText(context, displayText, x + height / 2 + 2, y + height / 3, 0.7F, false);
                }
            }
            else {
                RenderUtils.drawText(context, getDefaultText(), x + height / 2 + 2, y + height / 3, 0.7F, false);
            }
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
