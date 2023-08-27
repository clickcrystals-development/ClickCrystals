package io.github.itzispyder.clickcrystals.client.system;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.ClickCrystals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import static io.github.itzispyder.clickcrystals.ClickCrystals.prefix;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/cc-bulletin">https://itzispyder.github.io/cc-info</a>
 */
public class ClickCrystalsInfo {

    public static final String URL = "https://itzispyder.github.io/cc-info";
    private final String latest;
    private final ClickCrystalsUser[] owners;
    private final ClickCrystalsUser[] staffs;

    public ClickCrystalsInfo(String latest, ClickCrystalsUser[] staffs, ClickCrystalsUser[] owners) {
        this.latest = latest;
        this.owners = owners;
        this.staffs = staffs;
    }

    public String getLatest() {
        return latest;
    }

    public ClickCrystalsUser[] getOwners() {
        return owners;
    }

    public ClickCrystalsUser[] getStaffs() {
        return staffs;
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

            System.out.println(prefix + " Info requested: " + gson.toJson(ClickCrystals.info));
            ClickCrystals.info = info;
            return info;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ClickCrystals.info;
        }
    }

    /**
     * Try not to instantiate this class, parse it from json!
     * This will be read off of <a href="https://itzispyder.github.io/cc-bulletin">https://itzispyder.github.io/cc-info</a>
     */
    public static class ClickCrystalsUser {
        private final String name;
        private final UUID id;

        public ClickCrystalsUser(String name, UUID id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public UUID getId() {
            return id;
        }
    }
}
