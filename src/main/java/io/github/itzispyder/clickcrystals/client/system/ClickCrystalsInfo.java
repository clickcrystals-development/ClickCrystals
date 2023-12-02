package io.github.itzispyder.clickcrystals.client.system;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.ClickCrystals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin">https://itzispyder.github.io/clickcrystals/info</a>
 */
public class ClickCrystalsInfo {

    public static final String URL = "https://itzispyder.github.io/clickcrystals/info";
    private final String latest;
    private final ClickCrystalsUser[] owners;
    private final ClickCrystalsUser[] staffs;
    private final ClickCrystalsUser[] donators;

    public ClickCrystalsInfo(String latest) {
        this.latest = latest;
        owners = staffs = donators = new ClickCrystalsUser[]{};
    }

    public Version getLatest() {
        return Version.ofString(latest);
    }

    public ClickCrystalsUser[] getOwners() {
        return owners;
    }

    public ClickCrystalsUser[] getStaffs() {
        return staffs;
    }

    public ClickCrystalsUser[] getDonators() {
        return donators;
    }

    public ClickCrystalsUser getOwner(UUID id) {
        for (ClickCrystalsUser owner : owners) {
            if (owner.id.toString().equals(id.toString())) {
                return owner;
            }
        }

        return null;
    }

    public ClickCrystalsUser getOwner(String name) {
        for (ClickCrystalsUser owner : owners) {
            if (owner.name.equalsIgnoreCase(name)) {
                return owner;
            }
        }

        return null;
    }

    public ClickCrystalsUser getStaff(UUID id) {
        for (ClickCrystalsUser staff : staffs) {
            if (staff.id.toString().equals(id.toString())) {
                return staff;
            }
        }

        return null;
    }

    public ClickCrystalsUser getStaff(String name) {
        for (ClickCrystalsUser staff : staffs) {
            if (staff.name.equalsIgnoreCase(name)) {
                return staff;
            }
        }

        return null;
    }

    public ClickCrystalsUser getDonator(UUID id) {
        for (ClickCrystalsUser donator : donators) {
            if (donator.id.toString().equals(id.toString())) {
                return donator;
            }
        }

        return null;
    }

    public ClickCrystalsUser getDonator(String name) {
        for (ClickCrystalsUser donator : donators) {
            if (donator.name.equalsIgnoreCase(name)) {
                return donator;
            }
        }

        return null;
    }

    public static ClickCrystalsInfo request() {
        try {
            URL u = new URL(URL);
            InputStreamReader ir = new InputStreamReader(u.openStream());
            BufferedReader br = new BufferedReader(ir);
            Gson gson = new Gson();
            ClickCrystalsInfo info = gson.fromJson(br, ClickCrystalsInfo.class);

            if (info == null) {
                throw new IllegalStateException("json parse failed");
            }

            system.println("<- Info requested from '%s'".formatted(URL));
            ClickCrystals.info = info;
            return info;
        }
        catch (Exception ex) {
            system.println("X<- request failed");
            return ClickCrystals.info;
        }
    }

    /**
     * Try not to instantiate this class, parse it from json!
     * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin">https://itzispyder.github.io/clickcrystals/info</a>
     */
    public record ClickCrystalsUser(String name, UUID id) {

    }
}
