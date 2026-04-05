package io.github.itzispyder.clickcrystals.client.system;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.screens.BanScreen;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

import java.util.UUID;

public class ClickCrystalsGate implements Global {

    public ClickCrystalsGate() {

    }

    public ClickCrystalsInfo info() {
        return ClickCrystals.info;
    }

    public boolean isBanned() {
        system.logger.log("AUTH", "<- Checking ClickCrystals Authentication...");
        system.logger.log("AUTH", "Current session: " + getSessionUser().get());
        system.logger.log("AUTH", "Ban session (null if not banned): " + getBanSession().get());
        return getBanSession().isPresent();
    }

    public Voidable<ClickCrystalsInfo.Ban> getBanSession() {
        try {
            UUID id = Minecraft.getInstance().getUser().getProfileId();
            return Voidable.of(info().getBlacklisted(id));
        }
        catch (Exception ex) {
            system.printErr("Error loading ban session");
            system.printErr(ex.getMessage());
            return Voidable.of(null);
        }
    }

    public Voidable<ClickCrystalsInfo.User> getSessionUser() {
        try {
            User ses = Minecraft.getInstance().getUser();
            UUID id = ses.getProfileId();
            String name = ses.getName();
            return Voidable.of(new ClickCrystalsInfo.User(name, id));
        }
        catch (Exception ex) {
            system.printErr("Error loading session user");
            system.printErr(ex.getMessage());
            return Voidable.of(null);
        }
    }

    public void banishCurrentSession() {
        try {
            Minecraft mc = Minecraft.getInstance();
            mc.execute(() -> mc.disconnect(new BanScreen(), false, true));
        }
        catch (Exception ex) {
            system.printErr("Logging ClickCrystals user out: user is banned!");
            System.exit(0);
        }
    }

    public void printBanList() {
        system.logger.log("AUTH", "ClickCrystals Ban List: ");
        for (ClickCrystalsInfo.Ban ban : info().getBlacklisted()) {
            system.logger.log("AUTH", "Username: %s, UUID: %s".formatted(ban.user().name(), ban.user().id()));
        }
    }
}
