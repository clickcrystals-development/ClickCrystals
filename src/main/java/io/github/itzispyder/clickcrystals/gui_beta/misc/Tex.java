package io.github.itzispyder.clickcrystals.gui_beta.misc;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import net.minecraft.util.Identifier;

public final class Tex {
    private static final String modId = ClickCrystals.modId;

    public static final Identifier ICON = new Identifier(modId, "icon.png");

    public static class Shapes {
        public static final Identifier CIRCLE_DARK_GRAY = new Identifier(modId, "textures/display/shapes/circle/circle_dark_gray.png");
        public static final Identifier CIRCLE_GRAY = new Identifier(modId, "textures/display/shapes/circle/circle_gray.png");
        public static final Identifier CIRCLE_LIGHT_GRAY = new Identifier(modId, "textures/display/shapes/circle/circle_light_gray.png");
        public static final Identifier CIRCLE_LIGHT = new Identifier(modId, "textures/display/shapes/circle/circle_light.png");
        public static final Identifier CIRCLE_BLUE = new Identifier(modId, "textures/display/shapes/circle/circle_blue.png");
        public static final Identifier CIRCLE_BLUE_LOW = new Identifier(modId, "textures/display/shapes/circle/circle_blue_low.png");
        public static final Identifier CIRCLE_BLACK = new Identifier(modId, "textures/display/shapes/circle/circle_black.png");

        public static Identifier getCircle(Gray gray) {
            switch (gray) {
                case DARK_GRAY -> {
                    return CIRCLE_DARK_GRAY;
                }
                case LIGHT_GRAY -> {
                    return CIRCLE_LIGHT_GRAY;
                }
                case LIGHT -> {
                    return CIRCLE_LIGHT;
                }
                case GENERIC -> {
                    return CIRCLE_BLUE;
                }
                case GENERIC_LOW -> {
                    return CIRCLE_BLUE_LOW;
                }
                case BLACK -> {
                    return CIRCLE_BLACK;
                }
                default -> {
                    return CIRCLE_GRAY;
                }
            }
        }
    }


    public static class Icons {
        public static final Identifier ANNOUNCE = new Identifier(modId, "textures/display/icons/announcement.png");
        public static final Identifier HOME = new Identifier(modId, "textures/display/icons/home.png");
        public static final Identifier MODULES = new Identifier(modId, "textures/display/icons/modules.png");
        public static final Identifier SETTINGS = new Identifier(modId, "textures/display/icons/settings.png");
        public static final Identifier LOADING = new Identifier(modId, "textures/display/icons/loading.png");
    }
}
