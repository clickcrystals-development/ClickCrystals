package io.github.itzispyder.clickcrystals.gui.misc;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.util.Identifier;

public final class Tex implements Global {

    public static final Identifier ICON = new Identifier(modId, "icon.png");
    public static final Identifier ICON_PREMIUM = new Identifier(modId, "icon_premium.png");
    public static final Identifier ICON_CLICKSCRIPT = new Identifier(modId, "icon_clickscript.png");
    public static final Identifier ICON_NEW = new Identifier(modId, "icon_new-old_round.png");

    public static class Icons {
        public static final Identifier ANNOUNCE = new Identifier(modId, "textures/display/icons/announcement.png");
        public static final Identifier HOME = new Identifier(modId, "textures/display/icons/home.png");
        public static final Identifier MODULES = new Identifier(modId, "textures/display/icons/modules.png");
        public static final Identifier SETTINGS = new Identifier(modId, "textures/display/icons/settings.png");
        public static final Identifier LOADING = new Identifier(modId, "textures/display/icons/loading.png");
        public static final Identifier RESET = new Identifier(modId, "textures/display/icons/reset.png");
        public static final Identifier DOWNLOAD = new Identifier(modId, "textures/display/icons/download.png");
    }

    public static class Models {
        public static final Identifier CLICKCRYSTALS_CAPE = new Identifier(modId, "textures/model_screenshots/clickcrystals_cape.png");
        public static final Identifier CLICKCRYSTALS_CAPE_DEV = new Identifier(modId, "textures/model_screenshots/clickcrystals_cape_dev.png");
        public static final Identifier CLICKCRYSTALS_CAPE_DONO = new Identifier(modId, "textures/model_screenshots/clickcrystals_cape_donator.png");
    }

    public static class Defaults {
        public static final Identifier OPTIONS_BACKGROUND = Identifier.of(modId, "textures/gui/options_background.png");
        public static final Identifier NO_TOTEMS_MEME = Identifier.of(modId, "textures/overlays/no_totem.png");
        public static final Identifier NO_TOTEMS_ICON = Identifier.of(modId, "textures/overlays/totemless_icon.png");
    }

    public static class Socials {
        public static final Identifier DISCORD = Identifier.of(modId, "textures/display/socials/discord.png");
        public static final Identifier CURSEFORGE = Identifier.of(modId, "textures/display/socials/curseforge.png");
        public static final Identifier YOUTUBE = Identifier.of(modId, "textures/display/socials/youtube.png");
        public static final Identifier PLANETMC = Identifier.of(modId, "textures/display/socials/planetmc.png");
    }

    public static class Backdrops {
        public static final Identifier BACKDROP_HOME = Identifier.of(modId, "textures/gui/backdrop/backdrop_3.png");
        public static final Identifier BACKDROP_INV = Identifier.of(modId, "textures/gui/backdrop/backdrop_0.png");
    }

    public static class Overlays {
        public static final Identifier DIRECTION = new Identifier(modId, "textures/overlays/direction.png");
    }
}