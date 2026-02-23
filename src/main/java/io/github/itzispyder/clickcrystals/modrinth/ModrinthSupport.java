package io.github.itzispyder.clickcrystals.modrinth;

import io.github.itzispyder.clickcrystals.gui.misc.ChatColor;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.StringUtils;

public class ModrinthSupport {

    // change this field to update modrinth support status

    public static final boolean active = false; // set to true to active modrinth TOS support


    // helper methods and stuff
    public static final String warning = StringUtils.forceColor("The Modrinth version of this mod has prevented the use of this module, please download the original version of ClickCrystals via linked sources on our client's home page!", ChatColor.YELLOW);

    public static boolean isBlacklisted(Class<? extends Module> modType) {
        return active && modType.getAnnotation(ModrinthNoNo.class) != null;
    }

    public static boolean isBlacklisted(Module mod) {
        return isBlacklisted(mod.getClass());
    }
}
