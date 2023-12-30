package io.github.itzispyder.clickcrystals.client.client;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsInfo;
import io.github.itzispyder.clickcrystals.gui.screens.BanScreen;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

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
            UUID id = StringUtils.toUUID(MinecraftClient.getInstance().getSession().getUuid());
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
            Session ses = MinecraftClient.getInstance().getSession();
            UUID id = StringUtils.toUUID(ses.getUuid());
            String name = ses.getUsername();
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
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.execute(() -> mc.disconnect(new BanScreen()));
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
