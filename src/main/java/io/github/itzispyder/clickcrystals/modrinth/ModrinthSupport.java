package io.github.itzispyder.clickcrystals.modrinth;

import io.github.itzispyder.clickcrystals.modules.Module;

public class ModrinthSupport {

    // change this field to update modrinth support status

    public static final boolean active = false; // set to true to active modrinth TOS support


    // helper methods and stuff

    public static final String warning = "§eThe Modrinth version of this §emod has prevented the use §eof this module, please §edownload the original version of §eClickCrystals via linked §esources on our client's §ehome page!";

    public static boolean isBlacklisted(Class<? extends Module> modType) {
        if (!active) {
            return false;
        }
        return modType.getAnnotation(ModrinthNoNo.class) != null;
    }

    public static boolean isBlacklisted(Module mod) {
        return isBlacklisted(mod.getClass());
    }
}
