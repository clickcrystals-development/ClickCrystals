package io.github.itzispyder.clickcrystals.gui;

import net.minecraft.util.Identifier;

import static io.github.itzispyder.clickcrystals.ClickCrystals.modId;

public final  class TexturesIdentifiers {

    // default
    public static final Identifier ICON = new Identifier(modId, "textures/icon.png");

    // gui
    public static final Identifier ICON_BANNER = new Identifier(modId, "textures/gui/screen_title_banner.png");
    public static final Identifier SMOOTH_VERTICAL_WIDGET = new Identifier(modId, "textures/gui/smooth_widget_vertical.png");
    public static final Identifier SMOOTH_HORIZONTAL_WIDGET = new Identifier(modId, "textures/gui/smooth_widget_horizontal.png");
    public static final Identifier HOLLOW_VERTICAL_WIDGET = new Identifier(modId, "textures/gui/hollow_widget_vertical.png");
    public static final Identifier HOLLOW_HORIZONTAL_WIDGET = new Identifier(modId, "textures/gui/hollow_widget_horizontal.png");
    public static final Identifier SMOOTH_BACKGROUND = new Identifier(modId, "textures/gui/smooth_background.png");
    public static final Identifier SMOOTH_BANNER = new Identifier(modId, "textures/gui/smooth_banner.png");

    // icons
    public static final Identifier SEARCH = new Identifier(modId, "textures/gui/icons/search.png");
    public static final Identifier HOME = new Identifier(modId, "textures/gui/icons/home.png");

    // modules
    public static final Identifier MODULE_ON = new Identifier(modId, "textures/gui/modules/module_on.png");
    public static final Identifier MODULE_OFF = new Identifier(modId, "textures/gui/modules/module_off.png");
    public static final Identifier MODULE_EMPTY = new Identifier(modId, "textures/gui/modules/module_empty.png");

}
