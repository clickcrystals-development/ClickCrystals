package io.github.itzispyder.clickcrystals.client.client;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CapeManager {

    private final List<String> owners, staffs, donators;

    public CapeManager() {
        owners = staffs = donators = new ArrayList<>();
    }

    public void reloadTextures() {
        CompletableFuture.runAsync(() -> {
            if (ClickCrystals.info != null) {
                owners.clear();
                staffs.clear();
                donators.clear();

                owners.addAll(Arrays.stream(ClickCrystals.info.getOwners()).map(user -> user.getId().toString()).toList());
                staffs.addAll(Arrays.stream(ClickCrystals.info.getStaffs()).map(user -> user.getId().toString()).toList());
                donators.addAll(Arrays.stream(ClickCrystals.info.getDonators()).map(user -> user.getId().toString()).toList());
            }
        });
    }

    public Identifier getCapeTexture(GameProfile profile) {
        String id = profile.getId().toString();

        if (owners.contains(id)) {
            return GuiTextures.CLICKCRYSTALS_CAPE_DEV;
        }
        else if (staffs.contains(id)) {
            return GuiTextures.CLICKCRYSTALS_CAPE;
        }
        else if (donators.contains(id)) {
            return GuiTextures.CLICKCRYSTALS_CAPE_DONO;
        }

        return null;
    }
}
