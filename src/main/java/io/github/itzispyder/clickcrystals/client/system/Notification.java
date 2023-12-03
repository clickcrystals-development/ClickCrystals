package io.github.itzispyder.clickcrystals.client.system;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Notification implements Global {

    public static final ConcurrentLinkedQueue<Notification> QUEUE = new ConcurrentLinkedQueue<>();
    public static final int DISPLAY_WIDTH = 420;
    public static final int DISPLAY_HEIGHT = 30;
    private final AtomicInteger translation = new AtomicInteger(-100);
    private final AtomicBoolean showing = new AtomicBoolean(false);
    private final AtomicBoolean dead = new AtomicBoolean(false);
    private final String name, id, title, text;
    private final Gray backgroundColor, borderColor;
    private final Identifier icon;
    private final long stayTime;
    private long startTime = - 1;

    public Notification(String id, Identifier icon, Gray backgroundColor, Gray borderColor, String title, String text, long stayTime) {
        this.id = id;
        this.name = StringUtils.capitalizeWords(id);
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.title = StringUtils.color(title);
        this.text = StringUtils.color(text);
        this.stayTime = stayTime;
        this.icon = icon;
    }

    public void render(DrawContext context) {
        if (!showing.get()) {
            return;
        }

        Window win = mc.getWindow();
        int w = DISPLAY_WIDTH;
        int h = DISPLAY_HEIGHT;
        int x = win.getScaledWidth() / 2 - w / 2;
        int y = 10 + translation.get();

        RoundRectBrush.drawRoundRect(context, x, y, w, h, 5, borderColor);

        w -= 4;
        h -= 4;
        x += 2;
        y += 2;
        RoundRectBrush.drawRoundRect(context, x, y, w, h, 5, backgroundColor);

        w -= 10;
        h -= 10;
        x += 5;
        y += 5;
        RenderUtils.drawTexture(context, icon, x, y, h, h);

        x += h + 5;
        RenderUtils.drawText(context, title, x, y, 1.0F, true);

        y += 10;
        RenderUtils.drawText(context, "§7" + text, x, y, 0.9F, false);
    }

    public void sendToClient() {
        if (dead.get()) {
            system.printErrF("attempted to send a dead notification with id '%s' to client!", id);
            return;
        }
        if (!QUEUE.contains(this)) {
            QUEUE.add(this);
        }
    }

    public void tryScheduleDisplay() {
        if (showing.get()) {
            return;
        }

        showing.set(true);
        system.scheduler.runChainTask()
                .thenRepeat(translation::getAndIncrement, 1, 100)
                .thenWait(stayTime)
                .thenRepeat(() -> {
                    translation.getAndDecrement();
                    translation.getAndDecrement();
                }, 1, 50)
                .thenRun(() -> {
                    QUEUE.remove(this);
                    dead.set(true);
                    showing.set(false);
                })
                .startChain();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Gray getBackgroundColor() {
        return backgroundColor;
    }

    public Gray getBorderColor() {
        return borderColor;
    }

    public long getStayTime() {
        return stayTime;
    }

    public boolean isShowing() {
        return showing.get();
    }

    public boolean isDead() {
        return dead.get();
    }

    public Identifier getIcon() {
        return icon;
    }

    public long getTimeStayed() {
        return startTime == -1 ? 0 : System.currentTimeMillis() - startTime;
    }

    public void markStart() {
        startTime = System.currentTimeMillis();
    }


    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String id, title, text;
        private Gray backgroundColor, borderColor;
        private Identifier icon;
        private long stayTime;

        private Builder() {
            id = "unnamed-notification";
            title = text = "";
            backgroundColor = Gray.DARK_GRAY;
            borderColor = Gray.LIGHT_GRAY;
            icon = Tex.Icons.SETTINGS;
            stayTime = 6000;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder colorBackground(Gray color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder colorBorder(Gray color) {
            this.borderColor = color;
            return this;
        }

        public Builder icon(Identifier icon) {
            this.icon = icon;
            return this;
        }

        public Builder defIcon() {
            return icon(Tex.Icons.SETTINGS);
        }

        public Builder ccsIcon() {
            return icon(Tex.ICON_CLICKSCRIPT);
        }

        public Builder ccIcon() {
            return icon(Tex.ICON);
        }

        public Builder stayTime(long stayTime) {
            this.stayTime = stayTime;
            return this;
        }

        public Notification build() {
            return new Notification(id, icon, backgroundColor, borderColor, title, text, stayTime);
        }
    }
}
