package io.github.itzispyder.clickcrystals.gui.elements.browsingmode;

import io.github.itzispyder.clickcrystals.data.announce.Announcement;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.util.minecraft.TextUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class AnnouncementElement extends GuiElement {

    private final Announcement announcement;

    public AnnouncementElement(Announcement announcement, int x, int y) {
        super(x, y, 285, 30);
        this.announcement = announcement;

        int caret = y + 28;
        caret += TextUtils.wordWrap(announcement.desc(), width - 5 - 5, 0.7F).size() * 8;

        for (Announcement.Field field : announcement.fields()) {
            for (String line : TextUtils.wordWrap(field.desc(), width - 15 - 15, 0.7F)) {
                caret += 7;
            }
            caret += 22;
        }

        this.setHeight(caret - y);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        RenderUtils.fillRoundRect(context, x, y, width, height, 3, Shades.BLACK);

        int caret = y + 5;
        RenderUtils.drawText(context, announcement.title(), x + 5, caret, 0.8F, false);
        caret += 8;
        RenderUtils.drawHorLine(context, x + 5, caret, width - 10, Shades.GRAY);

        for (String line : TextUtils.wordWrap(announcement.desc(), width - 5 - 5, 0.7F)) {
            caret += 8;
            RenderUtils.drawText(context, line, x + 5, caret, 0.7F, false);
        }
        caret += 10;
        RenderUtils.drawHorLine(context, x + 5, caret, width / 10 * 8, Shades.GRAY);
        caret += 5;

        for (Announcement.Field field : announcement.fields()) {
            caret += 5;
            RenderUtils.drawText(context, field.title(), x + 15, caret, 0.7F, false);
            caret += 2;
            for (String line : TextUtils.wordWrap(field.desc(), width - 15 - 15, 0.7F)) {
                caret += 7;
                RenderUtils.drawText(context, "§7" + line, x + 15, caret, 0.7F, false);
            }
            caret += 15;
        }

        this.setHeight(caret - y);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public Announcement getAnnouncement() {
        return announcement;
    }
}
