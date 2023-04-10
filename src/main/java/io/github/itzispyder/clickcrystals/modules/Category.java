package io.github.itzispyder.clickcrystals.modules;

import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.util.Identifier;

import java.io.Serializable;

public class Category implements Serializable {

    private final String name;
    private final Identifier texture;
    private final IconWidget textureWidget;

    public Category(String name, Identifier texture) {
        this.name = name;
        this.texture = texture;
        this.textureWidget = new IconWidget(0,0,90,25,this.texture);
    }

    public IconWidget getTextureWidget() {
        return textureWidget;
    }

    public Identifier getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }
}
