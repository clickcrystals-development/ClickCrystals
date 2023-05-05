package io.github.itzispyder.clickcrystals.modules;

import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

import java.io.Serializable;

public class Category implements Serializable {

    private final String name;
    @Deprecated private final Identifier texture;
    @Deprecated private final TexturedButtonWidget textureWidget;

    public Category(String name, Identifier texture) {
        this.name = name;
        this.texture = texture;
        this.textureWidget = new TexturedButtonWidget(
                0,
                0,
                90,
                25,
                0,
                0,
                0,
                this.texture,
                90,
                25,
                (button) -> {}
        );
    }

    @Deprecated
    public TexturedButtonWidget getTextureWidget() {
        return textureWidget;
    }

    @Deprecated
    public Identifier getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }
}
