package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.gui.DrawContext;

public class NotificationHud extends Hud {

    private Voidable<Notification> notification = Voidable.of(null);
    private boolean readingNotification = false;

    public NotificationHud() {
        super("notifications-hud");
        this.setFixed(true);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (readingNotification && notification.isPresent() && !notification.get().isDead()) {
            notification.accept(n -> {
                n.render(context);
                n.tryScheduleDisplay();
            });
        }
        else {
            readingNotification = false;
            notification = Voidable.of(null);
        }

        if (!readingNotification && !Notification.QUEUE.isEmpty()) {
            readingNotification = true;
            notification = Voidable.of(Notification.QUEUE.poll());
        }
    }
}
