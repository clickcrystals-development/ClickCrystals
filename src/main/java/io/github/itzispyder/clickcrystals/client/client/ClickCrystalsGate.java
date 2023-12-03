package io.github.itzispyder.clickcrystals.client.client;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsInfo;
import io.github.itzispyder.clickcrystals.gui.screens.BanScreen;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.util.UUID;

public class ClickCrystalsGate {

    public ClickCrystalsGate() {

    }

    public ClickCrystalsInfo info() {
        return ClickCrystals.info;
    }

    public boolean isBanned() {
        ClickCrystals.system.println("[Auth] <- Checking ClickCrystals Authentication...");
        ClickCrystals.system.println("[Auth] Current session: " + getSessionUser().get());
        ClickCrystals.system.println("[Auth] Ban session (null if not banned): " + getBanSession().get());
        return getBanSession().isPresent();
    }

    public Voidable<ClickCrystalsInfo.Ban> getBanSession() {
        try {
            UUID id = StringUtils.toUUID(MinecraftClient.getInstance().getSession().getUuid());
            return Voidable.of(info().getBlacklisted(id));
        }
        catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
            return Voidable.of(null);
        }
    }

    public void banishCurrentSession() {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.execute(() -> mc.disconnect(new BanScreen()));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void printBanList() {
        ClickCrystals.system.println("ClickCrystals Ban List: ");
        for (ClickCrystalsInfo.Ban ban : info().getBlacklisted()) {
            ClickCrystals.system.printf("Username: %s, UUID: %s", ban.user().name(), ban.user().id());
        }
    }
}
