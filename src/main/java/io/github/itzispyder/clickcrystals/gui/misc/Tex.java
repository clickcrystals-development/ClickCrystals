package io.github.itzispyder.clickcrystals.gui.misc;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.resources.Identifier;

public final class Tex implements Global {

    public static final Identifier ICON = Identifier.tryBuild(modId, "icon.png");
    public static final Identifier ICON_PREMIUM = Identifier.tryBuild(modId, "icon_premium.png");
    public static final Identifier ICON_CLICKSCRIPT = Identifier.tryBuild(modId, "icon_clickscript.png");
    public static final Identifier ICON_NEW = Identifier.tryBuild(modId, "icon_new-old_round.png");

    public static class Icons {
        public static final Identifier ANNOUNCE = Identifier.tryBuild(modId, "textures/display/icons/announcement.png");
        public static final Identifier DOWNLOAD = Identifier.tryBuild(modId, "textures/display/icons/download.png");
        public static final Identifier FOLDER = Identifier.tryBuild(modId, "textures/display/icons/folder.png");
        public static final Identifier HOME = Identifier.tryBuild(modId, "textures/display/icons/home.png");
        public static final Identifier LOADING = Identifier.tryBuild(modId, "textures/display/icons/loading.png");
        public static final Identifier MODULES = Identifier.tryBuild(modId, "textures/display/icons/modules.png");
        public static final Identifier RESET = Identifier.tryBuild(modId, "textures/display/icons/reset.png");
        public static final Identifier SETTINGS = Identifier.tryBuild(modId, "textures/display/icons/settings.png");
    }

    public static class Models {
        public static final Identifier CLICKCRYSTALS_CAPE = Identifier.tryBuild(modId, "textures/models/clickcrystals_cape.png");
        public static final Identifier CLICKCRYSTALS_CAPE_DEV = Identifier.tryBuild(modId, "textures/models/clickcrystals_cape_dev.png");
        public static final Identifier CLICKCRYSTALS_CAPE_DONO = Identifier.tryBuild(modId, "textures/models/clickcrystals_cape_donator.png");
    }

    public static class Defaults {
        public static final Identifier OPTIONS_BACKGROUND = Identifier.tryBuild(modId, "textures/gui/options_background.png");
        public static final Identifier NO_TOTEMS_MEME = Identifier.tryBuild(modId, "textures/overlays/no_totem.png");
        public static final Identifier NO_TOTEMS_ICON = Identifier.tryBuild(modId, "textures/overlays/totemless_icon.png");
    }

    public static class Socials {
        public static final Identifier CURSEFORGE = Identifier.tryBuild(modId, "textures/display/socials/curseforge.png");
        public static final Identifier DISCORD = Identifier.tryBuild(modId, "textures/display/socials/discord.png");
        public static final Identifier PLANETMC = Identifier.tryBuild(modId, "textures/display/socials/planetmc.png");
        public static final Identifier YOUTUBE = Identifier.tryBuild(modId, "textures/display/socials/youtube.png");
    }

    public static class Backdrops {
        // BACKDROP_HOME (1024 x 400)
        public static final Identifier BACKDROP_HOME = Identifier.tryBuild(modId, "textures/gui/backdrop/backdrop_1.png");
        public static final Identifier BACKDROP_INV = Identifier.tryBuild(modId, "textures/gui/backdrop/backdrop_0.png");
    }

    public static class Overlays {
        public static final Identifier DIRECTION = Identifier.tryBuild(modId, "textures/overlays/direction.png");
    }
}
