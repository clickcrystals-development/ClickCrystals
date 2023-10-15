package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.data.announce.BulletinBoard;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.elements.cc.AnnouncementElement;
import io.github.itzispyder.clickcrystals.gui.elements.cc.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.AbstractElement;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class BulletinScreen extends ClickCrystalsBase {

    private BulletinBoard bulletin;
    private AnnouncementElement current;
    private int viewIndex = 0;

    public BulletinScreen() {
        super("ClickCrystals Bulletin");

        CompletableFuture<Void> f = BulletinBoard.request();
        int x = nav.x + nav.width + 10;
        int y = base.y + 10;
        int w = base.width - nav.width - 45;
        int h = base.height - 20;

        LoadingIconElement load = new LoadingIconElement(GuiTextures.ICON, x, y, w, h, 40);
        this.addChild(load);

        f.thenRun(() -> {
            if (f.isDone() && BulletinBoard.isCurrentValid()) {
                this.bulletin = BulletinBoard.getCurrent();
            }
            else {
                this.bulletin = BulletinBoard.createNull();
            }

            load.setRendering(false);
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
                    if (canPrev()) {
                        prev();
                        updateCurrent();
                    }
                })
                .build();

        AbstractElement dis = AbstractElement.create()
                .pos(up.x, up.y + up.height + 5)
                .dimensions(12, 12)
                .onRender((context, mouseX, mouseY, button) -> {
                    String text = (viewIndex + 1) + "/" + (bulletin == null ? 0 : bulletin.size());
                    RenderUtils.drawCenteredText(context, text, button.x + button.width / 2, button.y + (int)(button.height * 0.33), 0.8F, true);
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
                    if (canNext()) {
                        next();
                        updateCurrent();
                    }
                })
                .build();

        this.addChild(up);
        this.addChild(dis);
        this.addChild(down);
    }

    private void updateCurrent() {
        current.setAnnouncement(bulletin.getAnnouncements()[viewIndex]);
    }

    private boolean canNext() {
        return viewIndex + 1 < bulletin.size();
    }

    private boolean canPrev() {
        return viewIndex - 1 >= 0;
    }

    private void next() {
        if (bulletin != null && canNext()) {
            viewIndex++;
        }
    }

    private void prev() {
        if (bulletin != null && canPrev()) {
            viewIndex--;
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        this.close();
        client.setScreen(new BulletinScreen());
    }
}
