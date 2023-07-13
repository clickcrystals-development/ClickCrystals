package io.github.itzispyder.clickcrystals.gui.elements.ui;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.PressAction;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class DetailedButtonElement extends GuiElement {

    private Identifier texture, icon;
    private String title, subtitle;
    private float textScale;
    private PressAction<DetailedButtonElement> pressAction;

    public DetailedButtonElement(Identifier texture, Identifier icon, int x, int y, int width, int height, String title, String subtitle, float textScale, PressAction<DetailedButtonElement> pressAction) {
        super(x, y, width, height);
        this.pressAction = pressAction;
        this.texture = texture;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.textScale = textScale;

        ImageElement bg = new ImageElement(texture, x, y, width, height);
        ImageElement iconImg = new ImageElement(icon, x + 2, y + height / 4, height / 2, height / 2);
        TextElement titleText = new TextElement(title, TextAlignment.LEFT, textScale, iconImg.x + iconImg.width + 2, iconImg.y);
        TextElement subtitleText = new TextElement(subtitle, TextAlignment.LEFT, textScale / 2, iconImg.x + iconImg.width + 2, titleText.y + titleText.height + 3);
        this.addChild(bg);
        this.addChild(iconImg);
        this.addChild(titleText);
        this.addChild(subtitleText);
    }

    public DetailedButtonElement(Identifier texture, Identifier icon, int x, int y, int width, int height, String title, String subtitle, float textScale) {
        this(texture, icon, x, y, width, height, title, subtitle, textScale, button -> {});
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        pressAction.onPress(this);
    }

    public Identifier getTexture() {
        return texture;
    }

    public void setTexture(Identifier texture) {
        this.texture = texture;
    }

    public Identifier getIcon() {
        return icon;
    }

    public void setIcon(Identifier icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public float getTextScale() {
        return textScale;
    }

    public void setTextScale(float textScale) {
        this.textScale = textScale;
    }

    public PressAction<DetailedButtonElement> getPressAction() {
        return pressAction;
    }

    public void setPressAction(PressAction<DetailedButtonElement> pressAction) {
        this.pressAction = pressAction;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int x, y, width, height;
        private Identifier texture, icon;
        private String title, subtitle;
        private float textScale;
        private PressAction<DetailedButtonElement> pressAction, onBuild;

        public Builder() {
            texture = icon = GuiTextures.SMOOTH_HORIZONTAL_WIDGET;
            title = subtitle = "";
            textScale = 1.0F;
            pressAction = onBuild = button -> {};
            x = y = width = height = 0;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder dimensions(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder texture(Identifier texture) {
            this.texture = texture;
            return this;
        }

        public Builder icon(Identifier icon) {
            this.icon = icon;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder textScale(float textScale) {
            this.textScale = textScale;
            return this;
        }

        public Builder onPress(PressAction<DetailedButtonElement> pressAction) {
            this.pressAction = pressAction;
            return this;
        }

        public Builder onBuild(PressAction<DetailedButtonElement> onBuild) {
            this.onBuild = onBuild;
            return this;
        }

        public DetailedButtonElement build() {
            DetailedButtonElement built =  new DetailedButtonElement(texture, icon, x, y, width, height, title, subtitle, textScale, pressAction);
            onBuild.onPress(built);
            return built;
        }
    }
}
