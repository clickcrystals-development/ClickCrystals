package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.data.announce.Announcement;
import io.github.itzispyder.clickcrystals.data.announce.BulletinBoard;
import io.github.itzispyder.clickcrystals.gui_beta.elements.client.AnnouncementElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.concurrent.CompletableFuture;

public class BulletinScreen extends DefaultBase {

    private BulletinBoard bulletin;

    public BulletinScreen() {
        super("Bulletin Screen");

        LoadingIconElement loadingIcon = new LoadingIconElement(contentX + contentWidth / 2, contentY + contentHeight / 2, 20);
        this.addChild(loadingIcon);

        CompletableFuture<Void> f = BulletinBoard.request();
        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);

        f.thenRun(() -> {
            if (f.isDone() && BulletinBoard.isCurrentValid()) {
                this.bulletin = BulletinBoard.getCurrent();
            }
            else {
                this.bulletin = BulletinBoard.createNull();
            }
            if (bulletin.getAnnouncements().length == 0) {
                return;
            }

            loadingIcon.setRendering(false);

            int caret = contentY + 21;
            int margin = contentX + 5;

            for (int i = bulletin.getAnnouncements().length - 1; i >= 0; i--) {
                Announcement announcement = bulletin.getAnnouncements()[i];
                caret += 5;
                AnnouncementElement ae = new AnnouncementElement(announcement, margin, caret);
                panel.addChild(ae);
                caret += ae.height;
            }
            this.addChild(panel);
        });
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawText(context, "Announcements Bulletin", contentX + 10, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new BulletinScreen());
    }
}
