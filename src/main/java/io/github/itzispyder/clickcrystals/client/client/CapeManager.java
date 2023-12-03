package io.github.itzispyder.clickcrystals.client.client;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsInfo;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CapeManager {

    private final List<String> owners, staffs, donators;

    public CapeManager() {
        owners = new ArrayList<>();
        staffs = new ArrayList<>();
        donators = new ArrayList<>();
    }

    public void reloadTextures() {
        CompletableFuture.runAsync(() -> {
            if (ClickCrystals.info != null) {
                Function<ClickCrystalsInfo.User, String> TO_STRING = user -> user.id().toString();
                owners.clear();
                staffs.clear();
                donators.clear();

                owners.addAll(Arrays.stream(ClickCrystals.info.getOwners()).map(TO_STRING).toList());
                staffs.addAll(Arrays.stream(ClickCrystals.info.getStaffs()).map(TO_STRING).toList());
                donators.addAll(Arrays.stream(ClickCrystals.info.getDonators()).map(TO_STRING).toList());
            }
        });
    }

    public Identifier getCapeTexture(GameProfile profile) {
        String id = profile.getId().toString();

        if (owners.contains(id)) {
            return Tex.Models.CLICKCRYSTALS_CAPE_DEV;
        }
        else if (staffs.contains(id)) {
            return Tex.Models.CLICKCRYSTALS_CAPE;
        }
        else if (donators.contains(id)) {
            return Tex.Models.CLICKCRYSTALS_CAPE_DONO;
        }

        return null;
    }
}
