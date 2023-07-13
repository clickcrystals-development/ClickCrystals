package io.github.itzispyder.clickcrystals.gui.elements.cc.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class StringSettingElement extends GuiElement {

    private final StringSetting setting;
    private String input;
    private final float textScale;
    private final ImageElement exButton;

    public StringSettingElement(StringSetting setting, int x, int y, int width, int height, float textScale) {
        super(x, y, width, height);
        this.setting = setting;
        this.textScale = textScale;
        this.input = setting.getVal();

        ImageElement bg = new ImageElement(GuiTextures.SETTING_STRING, x, y, width, height);
        TextElement title = new TextElement(setting.getName(), TextAlignment.LEFT, 0.5F, x + 100, y);
        TextElement desc = new TextElement("§7" + setting.getDescription(), TextAlignment.LEFT, 0.45F, title.x, title.y + 5);
        this.addChild(title);
        this.addChild(desc);
        this.addChild(bg);

        exButton = new ImageElement(GuiTextures.X, x + width - height, y, height, height);
        this.addChild(exButton);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            String text = input;

            while (text.length() > 0 && mc.textRenderer.getWidth(text) * textScale > width - height - 2) {
                text = text.substring(1);
            }

            String displayText = screen.selected == this ? text + "︳" : text;
            DrawableUtils.drawText(context, displayText, x + 2, y + (int)(height * 0.33), textScale, true);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        mc.player.playSound(SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.MASTER, 0.8F, 2);

        if (mc.currentScreen instanceof GuiScreen screen) {
            if (exButton.isHovered((int)mouseX, (int)mouseY)) {
                this.input = "";
                screen.selected = exButton;
            }
            else {
                screen.selected = this;
            }
        }

        setting.setVal(input);
    }

    public void onKey(int key, int scancode) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            String typed = GLFW.glfwGetKeyName(key, scancode);

            if (key == GLFW.GLFW_KEY_ESCAPE) {
                screen.selected = null;
            }
            else if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (input.length() > 0) {
                    input = input.substring(0, input.length() - 1);
                }
            }
            else if (key == GLFW.GLFW_KEY_SPACE) {
                input = input.concat(" ");
            }
            else if (typed != null){
                input = input.concat(screen.shiftKeyPressed ? typed.toUpperCase() : typed);
            }

            setting.setVal(input);
        }
    }

    public String getInput() {
        return input;
    }

    public StringSetting getSetting() {
        return setting;
    }

    public int getTextHeight() {
        return (int)(10 * textScale);
    }

    public float getTextScale() {
        return textScale;
    }
}
