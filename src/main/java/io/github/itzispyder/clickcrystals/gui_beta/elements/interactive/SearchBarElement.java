package io.github.itzispyder.clickcrystals.gui_beta.elements.interactive;

import io.github.itzispyder.clickcrystals.gui_beta.ClickType;
import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.callbacks.KeyPressCallback;
import io.github.itzispyder.clickcrystals.gui_beta.elements.Typeable;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class SearchBarElement extends GuiElement implements Typeable {

    public final List<KeyPressCallback> keyPressCallbacks = new ArrayList<>();
    private String query;

    public SearchBarElement(int x, int y, int width) {
        super(x, y, width, 12);
        this.query = "";
    }

    public SearchBarElement(int x, int y) {
        this(x, y, 90);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
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
                RenderUtils.drawText(context, "§7Search module i.e.", x + height / 2 + 2, y + height / 3, 0.7F, false);
            }
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    @Override
    public void onKey(int key, int scancode) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            String typed = GLFW.glfwGetKeyName(key, scancode);

            if (key == GLFW.GLFW_KEY_ESCAPE) {
                screen.selected = null;
            }
            else if (key == GLFW.GLFW_KEY_BACKSPACE && !query.isEmpty()) {
                query = query.substring(0, query.length() - 1);
            }
            else if (key == GLFW.GLFW_KEY_SPACE) {
                query = query.concat(" ");
            }
            else if (key == GLFW.GLFW_KEY_V && screen.ctrlKeyPressed) {
                query = query.concat(StringUtils.fromClipboard());
            }
            else if (typed != null){
                query = query.concat(screen.shiftKeyPressed ? StringUtils.keyPressWithShift(typed) : typed);
            }

            for (KeyPressCallback callback : keyPressCallbacks) {
                callback.handleKey(key, ClickType.CLICK, scancode, -1);
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
}
