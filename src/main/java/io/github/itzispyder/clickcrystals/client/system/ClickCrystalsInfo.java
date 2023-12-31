package io.github.itzispyder.clickcrystals.client.system;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin">https://itzispyder.github.io/clickcrystals/info</a>
 */
public class ClickCrystalsInfo implements Global {

    public static final String URL = "https://itzispyder.github.io/clickcrystals/info";
    private final String latest;
    private final String motd;
    private final User[] owners;
    private final User[] staffs;
    private final User[] donators;
    private final Ban[] blacklisted;

    public ClickCrystalsInfo(Version latest) {
        this.latest = latest.getVersionString();
        motd = "";
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

    public String getMotd() {
        return StringUtils.color(motd);
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
        return collect(owners, staffs);
    }

    public List<User> collectBanned() {
        return Arrays.stream(blacklisted).map(Ban::user).toList();
    }

    public List<User> collectVip() {
        return collect(owners, staffs, donators);
    }

    public List<User> collectAllUsers() {
        List<User> l = collectVip();
        l.addAll(collectBanned());
        return l;
    }

    private List<User> collect(User... users) {
        List<User> l = new ArrayList<>();
        Collections.addAll(l, users);
        return l;
    }

    public List<String> names(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(User::name).toList();
    }

    public String namesStringLong(List<User> users) {
        return String.join(", ", names(users));
    }

    public String namesStringTall(List<User> users) {
        return String.join(",\n", names(users));
    }

    private List<User> collect(User[]... users) {
        List<User> l = new ArrayList<>();
        for (User[] user : users) {
            Collections.addAll(l, user);
        }
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
