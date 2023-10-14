package io.github.itzispyder.clickcrystals.gui_beta.misc;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.util.Identifier;

public final class Tex implements Global {

    public static class Shapes {
        public static final Identifier CIRCLE_DARK_GRAY = new Identifier(modId, "textures/display/shapes/circle/circle_dark_gray.png");
        public static final Identifier CIRCLE_GRAY = new Identifier(modId, "textures/display/shapes/circle/circle_gray.png");
        public static final Identifier CIRCLE_LIGHT_GRAY = new Identifier(modId, "textures/display/shapes/circle/circle_light_gray.png");
        public static final Identifier CIRCLE_LIGHT = new Identifier(modId, "textures/display/shapes/circle/circle_light.png");

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
                default -> {
                    return CIRCLE_GRAY;
                }
            }
        }
    }
}
