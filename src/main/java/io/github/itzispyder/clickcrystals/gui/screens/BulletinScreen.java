package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.data.announce.Announcement;
import io.github.itzispyder.clickcrystals.data.announce.BulletinBoard;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.cc.AnnouncementElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.AbstractElement;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class BulletinScreen extends ClickCrystalsBase {

    private BulletinBoard bulletin;
    private AnnouncementElement current;
    private static int viewIndex = 0;

    public BulletinScreen() {
        super("ClickCrystals Bulletin");

        CompletableFuture<Void> f = BulletinBoard.request();
        f.thenRun(() -> {
            if (f.isDone() && BulletinBoard.isCurrentValid()) {
                this.bulletin = BulletinBoard.getCurrent();
            }
            else {
                this.bulletin = BulletinBoard.createNull();
            }

            int x = nav.x + nav.width + 10;
            int y = base.y + 10;
            int w = base.width - nav.width - 45;
            int h = base.height - 20;

            current = new AnnouncementElement(this, bulletin.getAnnouncements()[0], x, y, w, h);
            this.addChild(current);
        });

        AbstractElement up = AbstractElement.create()
                .pos(base.x + base.width - 20, base.y + 12)
                .dimensions(12, 12)
                .onRender((context, mouseX, mouseY, button) -> {
                    Identifier tex = viewIndex <= 0 ? GuiTextures.ARROW_UP_DARK : GuiTextures.ARROW_UP;
                    context.drawTexture(tex, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
                })
                .onPress(button -> {
                    current.setAnnouncement(prev());
                })
                .build();

        AbstractElement dis = AbstractElement.create()
                .pos(up.x, up.y + up.height + 5)
                .dimensions(12, 12)
                .onRender((context, mouseX, mouseY, button) -> {
                    String text = (viewIndex + 1) + "/" + (bulletin == null ? 0 : bulletin.size());
                    DrawableUtils.drawCenteredText(context, text, button.x + button.width / 2, button.y + (int)(button.height * 0.33), 0.8F, true);
                })
                .build();

        AbstractElement down = AbstractElement.create()
                .pos(dis.x, dis.y + dis.height + 5)
                .dimensions(12, 12)
                .onRender((context, mouseX, mouseY, button) -> {
                    Identifier tex = bulletin == null || viewIndex >= bulletin.size() - 1 ? GuiTextures.ARROW_DOWN_DARK : GuiTextures.ARROW_DOWN;
                    context.drawTexture(tex, button.x, button.y, 0, 0, button.width, button.height, button.width, button.height);
                })
                .onPress(button -> {
                    current.setAnnouncement(next());
                })
                .build();

        this.addChild(up);
        this.addChild(dis);
        this.addChild(down);
    }

    private Announcement next() {
        if (bulletin != null) {
            viewIndex++;
            if (viewIndex >= bulletin.size()) {
                viewIndex = 0;
            }

            return bulletin.getAnnouncements()[viewIndex];
        }
        return BulletinBoard.createNull().getAnnouncements()[0];
    }

    private Announcement prev() {
        if (bulletin != null) {
            viewIndex--;
            if (viewIndex < 0) {
                viewIndex = bulletin.size() - 1;
            }

            return bulletin.getAnnouncements()[viewIndex];
        }
        return BulletinBoard.createNull().getAnnouncements()[0];
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        client.setScreen(new BulletinScreen());
    }
}
