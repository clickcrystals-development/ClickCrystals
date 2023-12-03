package io.github.itzispyder.clickcrystals.client.system;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.ClickCrystals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin">https://itzispyder.github.io/clickcrystals/info</a>
 */
public class ClickCrystalsInfo {

    public static final String URL = "https://itzispyder.github.io/clickcrystals/info";
    private final String latest;
    private final User[] owners;
    private final User[] staffs;
    private final User[] donators;
    private final Ban[] blacklisted;

    public ClickCrystalsInfo(String latest) {
        this.latest = latest;
        owners = staffs = donators = new User[]{};
        blacklisted = new Ban[]{};
    }

    public User getOwner(UUID id) {
        for (User user : owners) {
            if (user.id.toString().equals(id.toString())) {
                return user;
            }
        }
        return null;
    }

    public User getStaff(UUID id) {
        for (User user : staffs) {
            if (user.id.toString().equals(id.toString())) {
                return user;
            }
        }
        return null;
    }

    public User getDonator(UUID id) {
        for (User user : donators) {
            if (user.id.toString().equals(id.toString())) {
                return user;
            }
        }
        return null;
    }

    public Ban getBlacklisted(UUID id) {
        for (Ban ban : blacklisted) {
            if (ban.user.id.toString().equals(id.toString())) {
                return ban;
            }
        }
        return null;
    }

    public Version getLatest() {
        return Version.ofString(latest);
    }

    public User[] getOwners() {
        return owners;
    }

    public User[] getStaffs() {
        return staffs;
    }

    public User[] getDonators() {
        return donators;
    }

    public Ban[] getBlacklisted() {
        return blacklisted;
    }

    public List<User> collectOwners() {
        return Arrays.asList(owners);
    }

    public List<User> collectStaff() {
        List<User> l = Arrays.asList(owners);
        l.addAll(Arrays.asList(staffs));
        return l;
    }

    public List<User> collectBanned() {
        return Arrays.stream(blacklisted).map(Ban::user).toList();
    }

    public List<User> collectVip() {
        List<User> l = Arrays.asList(owners);
        l.addAll(Arrays.asList(staffs));
        l.addAll(Arrays.asList(donators));
        return l;
    }

    public List<User> collectAllUsers() {
        List<User> l = Arrays.asList(owners);
        l.addAll(Arrays.asList(staffs));
        l.addAll(Arrays.asList(donators));
        l.addAll(Arrays.stream(blacklisted).map(Ban::user).toList());
        return l;
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
    public record User(String name, UUID id) {
        @Override
        public String toString() {
            return "{\"name\": \"%s\", \"id\": \"%s\"}".formatted(name, id);
        }
    }

    /**
     * Try not to instantiate this class, parse it from json!
     * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin">https://itzispyder.github.io/clickcrystals/info</a>
     */
    public record Ban(String reason, User user) {
        @Override
        public String reason() {
            return reason == null || reason.trim().isEmpty() ? "Unspecific reason." : reason;
        }

        @Override
        public String toString() {
            return "{\"reason\": \"%s\", \"user\": \"%s\"}".formatted(reason, user);
        }
    }
}
