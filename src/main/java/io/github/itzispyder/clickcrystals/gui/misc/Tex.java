package io.github.itzispyder.clickcrystals.gui.misc;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.util.Identifier;

public final class Tex implements Global {

    public static final Identifier ICON = new Identifier(modId, "icon.png");
    public static final Identifier ICON_PREMIUM = new Identifier(modId, "icon_premium.png");
    public static final Identifier ICON_CLICKSCRIPT = new Identifier(modId, "icon_clickscript.png");
    public static final Identifier ICON_NEW = new Identifier(modId, "icon_new-old_round.png");

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
        public static final Identifier RESET = new Identifier(modId, "textures/display/icons/reset.png");
    }

    public static class Models {
        public static final Identifier CLICKCRYSTALS_CAPE = new Identifier(modId, "textures/model_screenshots/clickcrystals_cape.png");
        public static final Identifier CLICKCRYSTALS_CAPE_DEV = new Identifier(modId, "textures/model_screenshots/clickcrystals_cape_dev.png");
        public static final Identifier CLICKCRYSTALS_CAPE_DONO = new Identifier(modId, "textures/model_screenshots/clickcrystals_cape_donator.png");
    }

    public static class Defaults {
        public static final Identifier ITEM_WIDGET = new Identifier(modId, "textures/gui/item_widget.png");
        public static final Identifier OPTIONS_BACKGROUND = new Identifier(modId, "textures/gui/options_background.png");
        public static final Identifier NO_TOTEMS_MEME = new Identifier(modId, "textures/overlays/no_totem.png");
        public static final Identifier NO_TOTEMS_ICON = new Identifier(modId, "textures/overlays/totemless_icon.png");
    }

    public static class Socials {
        public static final Identifier DISCORD = new Identifier(modId, "textures/display/socials/discord.png");
        public static final Identifier MODRINTH = new Identifier(modId, "textures/display/socials/modrinth.png");
        public static final Identifier CURSEFORGE = new Identifier(modId, "textures/display/socials/curseforge.png");
        public static final Identifier YOUTUBE = new Identifier(modId, "textures/display/socials/youtube.png");
        public static final Identifier PLANETMC = new Identifier(modId, "textures/display/socials/planetmc.png");
    }

    public static class Backdrops {
        public static final Identifier BACKDROP_0 = new Identifier(modId, "textures/gui/backdrop/backdrop.png");
        public static final Identifier BACKDROP_1 = new Identifier(modId, "textures/gui/backdrop/backdrop_1.png");
    }

    public static class Overlays {
        public static final Identifier DIRECTION = new Identifier(modId, "textures/overlays/direction.png");
    }
}
