package io.github.itzispyder.clickcrystals.client.system;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin.json">https://itzispyder.github.io/clickcrystals/bulletin.json</a>
 */
public class BulletinBoard implements Global {

    private final Announcement[] announcements;
    private transient int unreadCount;

    public BulletinBoard(Announcement... announcements) {
        this.announcements = announcements;
        this.sort();
    }

    private static final AtomicReference<BulletinBoard> current = new AtomicReference<>(null);
    public static final String URL = "https://itzispyder.github.io/clickcrystals/bulletin.json";

    public int size() {
        return announcements.length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Announcement[] getAnnouncements() {
        return announcements;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void updateUnreadCount(int fetchedCount) {
        this.unreadCount = Math.max(0, fetchedCount - ClickCrystals.config.getReadAnnouncementCount());
    }

    public void markAllAsRead() {
        ClickCrystals.config.setReadAnnouncementCount(size());
        ClickCrystals.config.save();
        this.unreadCount = 0;
    }

    public static boolean hasCurrent() {
        return getCurrent() != null;
    }

    public static boolean isCurrentValid() {
        return hasCurrent() && !getCurrent().isEmpty();
    }

    public void sort() {
        for (int i = 0; i < announcements.length; i++) {
            int first = i;
            for (int j = i + 1; j < announcements.length; j++)
                if (announcements[j].order() > announcements[first].order())
                    first = j;

            Announcement temp = announcements[i];
            announcements[i] = announcements[first];
            announcements[first] = temp;
        }
    }

    /**
     * Should call hasCurrent() before this!
     *
     * @return BulletinBoard
     */
    public static BulletinBoard getCurrent() {
        return current.get();
    }

    /**
     * Runs async, might have to wait awhile.
     *
     * @return async future
     */
    public static CompletableFuture<BulletinBoard> request() {
        return CompletableFuture
                .supplyAsync(BulletinBoard::requestInternal)
                .thenApply(bulletinBoard ->  {
                    // ping if unread
                    bulletinBoard.updateUnreadCount(bulletinBoard.getAnnouncements().length);
                    current.set(bulletinBoard);
                    ClickCrystals.requestModInfo();
                    return bulletinBoard;
                })
                .exceptionally(error -> {
                    system.printErr("Error requesting bulletin board");
                    system.printErr(error.getMessage());
                    return createNull();
                });
    }

    private static synchronized BulletinBoard requestInternal() {
        try {
            URL u = URI.create(URL).toURL();
            InputStream is = u.openStream();
            String jsonString = new String(is.readAllBytes());

            is.close();

            BulletinBoard bulletin = new Gson().fromJson(jsonString, BulletinBoard.class);
            if (bulletin == null)
                throw new IllegalStateException("json parse failed!");
            return bulletin;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BulletinBoard createNull() {
        Announcement.Field f1 = new Announcement.Field("Try:", "1) Checking your wifi connection. 2) Checking the webpage, it might be down. 3) Contacting the client owners!");
        Announcement.Field f2 = new Announcement.Field("Contact:", "ImproperIssues#0 on Discord, or my Github. Also email (itzispyder@gmail.com) works too!");
        Announcement ann = new Announcement("&cWhoops, something went wrong...", "The bulletin page you've been looking for is empty or non-existent!", f1, f2);
        return new BulletinBoard(ann);
    }
}
