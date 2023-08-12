package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.Typeable;
import io.github.itzispyder.clickcrystals.gui.elements.base.WidgetElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class SearchBarElement extends GuiElement implements Typeable {

    private String query;
    private final float textScale;

    public SearchBarElement(int x, int y, int width, float textScale) {
        super(x, y, width, (int)(10 * textScale) + 4);
        this.textScale = textScale;
        this.query = "";

        WidgetElement bg = new WidgetElement(x, y, width, height);
        this.addChild(bg);
        ImageElement search = new ImageElement(GuiTextures.SEARCH, x + 2, y + 2, getTextHeight(), getTextHeight());
        this.addChild(search);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            String text = query;

            while (text.length() > 0 && mc.textRenderer.getWidth(text) * textScale > width - 4 - getTextHeight()) {
                text = text.substring(1);
            }

            String displayText = screen.selected == this ? text + "︳" : text;
            DrawableUtils.drawText(context, displayText, x + 4 + getTextHeight(), y + (int)(height * 0.33), textScale, true);
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
            else if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (query.length() > 0) {
                    query = query.substring(0, query.length() - 1);
                }
            }
            else if (key == GLFW.GLFW_KEY_SPACE) {
                query = query.concat(" ");
            }
            else if (typed != null){
                query = query.concat(screen.shiftKeyPressed ? typed.toUpperCase() : typed);
            }
        }
    }

    public String getQuery() {
        return query;
    }

    public String getLowercaseQuery() {
        return query.toLowerCase();
    }

    public int getTextHeight() {
        return (int)(10 * textScale);
    }

    public float getTextScale() {
        return textScale;
    }
}
