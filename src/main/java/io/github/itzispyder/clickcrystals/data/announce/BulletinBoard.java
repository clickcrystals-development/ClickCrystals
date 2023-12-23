package io.github.itzispyder.clickcrystals.data.announce;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.ClickCrystals;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Try not to instantiate this class, parse it from json!
 * This will be read off of <a href="https://itzispyder.github.io/clickcrystals/bulletin">https://itzispyder.github.io/clickcrystals/bulletin</a>
 */
public class BulletinBoard {

    private static AtomicReference<BulletinBoard> current = new AtomicReference<>(null);
    public static final String URL = "https://itzispyder.github.io/clickcrystals/bulletin";
    private final Announcement[] announcements;

    public BulletinBoard(Announcement... announcements) {
        this.announcements = announcements;
    }

    public Announcement[] getAnnouncements() {
        return announcements;
    }

    public int size() {
        return announcements.length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public static boolean hasCurrent() {
        return getCurrent() != null;
    }

    public static boolean isCurrentValid() {
        return hasCurrent() && !getCurrent().isEmpty();
    }

    /**
     * Should call hasCurrent() before this!
     * @return BulletinBoard
     */
    public static BulletinBoard getCurrent() {
        return current.get();
    }

    /**
     * Runs async, might have to wait awhile.
     * @return async future
     */
    public static synchronized CompletableFuture<Void> request() {
        return CompletableFuture.runAsync(BulletinBoard::get);
    }

    private static synchronized void get() {
        try {
            URL u = new URL(URL);
            InputStream is = u.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            Gson gson = new Gson();
            BulletinBoard bulletin = gson.fromJson(isr, BulletinBoard.class);

            if (bulletin == null) {
                throw new IllegalStateException("json parse failed!");
            }

            isr.close();
            current.set(bulletin);
            ClickCrystals.requestModInfo();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static BulletinBoard createNull() {
        Announcement.Field f1 = new Announcement.Field("Try:", "1) Checking your wifi connection. 2) Checking the webpage, it might be down. 3) Contacting the client owners!");
        Announcement.Field f2 = new Announcement.Field("Contact:", "ImproperIssues#0 on Discord, or my Github. Also email (itzispyder@gmail.com) works too!");
        Announcement ann = new Announcement("&cWhoops, something went wrong...", "The bulletin page you've been looking for is empty or non-existent!", f1, f2);
        return new BulletinBoard(ann);
    }
}
