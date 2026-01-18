package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.client.system.BulletinBoard;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.AnnouncementElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class BulletinScreen extends DefaultBase {

    public BulletinScreen() {
        super("Bulletin Screen");

        LoadingIconElement loadingIcon = new LoadingIconElement(contentX + contentWidth / 2 - 10, contentY + contentHeight / 2 - 10, 20);
        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);
        this.addChild(loadingIcon);

        BulletinBoard.request().thenAccept(bulletinBoard -> {
            loadingIcon.setRendering(false);
            bulletinBoard.markAllAsRead();

            int caret = contentY + 21;
            int margin = contentX + 5;

            AtomicInteger finalCaret = new AtomicInteger(caret);

            Arrays.stream(bulletinBoard.getAnnouncements()).forEach(announcement -> mc.execute(() -> {
                AnnouncementElement ae = new AnnouncementElement(announcement, margin, finalCaret.addAndGet(5));
                panel.addChild(ae);
                finalCaret.addAndGet(ae.getHeight());
            }));
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
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
    }

    @Override
    public void resize(int width, int height) {
        client.setScreen(new BulletinScreen());
    }
}
