package io.github.itzispyder.clickcrystals.gui.elements.browsingmode;

import io.github.itzispyder.clickcrystals.data.announce.Announcement;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class AnnouncementElement extends GuiElement {

    private final Announcement announcement;

    public AnnouncementElement(Announcement announcement, int x, int y) {
        super(x, y, 285, 30);
        this.announcement = announcement;

        int caret = y + 28;
        caret += StringUtils.wrapLines(announcement.desc(), 60, true).size() * 8;

        for (Announcement.Field field : announcement.fields()) {
            for (String line : StringUtils.wrapLines(field.desc(), 45, true)) {
                caret += 7;
            }
            caret += 22;
        }

        this.setHeight(caret - y);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        RoundRectBrush.drawRoundRect(context, x, y, width, height, 3, Gray.BLACK);

        int caret = y + 5;
        RenderUtils.drawText(context, announcement.title(), x + 5, caret, 0.8F, false);
        caret += 8;
        RenderUtils.drawHorizontalLine(context, x + 5, caret, width - 10, 1, Gray.GRAY.argb);

        for (String line : StringUtils.wrapLines(announcement.desc(), 60, true)) {
            caret += 8;
            RenderUtils.drawText(context, line, x + 5, caret, 0.7F, false);
        }
        caret += 10;
        RenderUtils.drawHorizontalLine(context, x + 5, caret, width / 10 * 8, 1, Gray.GRAY.argb);
        caret += 5;

        for (Announcement.Field field : announcement.fields()) {
            caret += 5;
            RenderUtils.drawText(context, field.title(), x + 15, caret, 0.7F, false);
            caret += 2;
            for (String line : StringUtils.wrapLines(field.desc(), 45, true)) {
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
