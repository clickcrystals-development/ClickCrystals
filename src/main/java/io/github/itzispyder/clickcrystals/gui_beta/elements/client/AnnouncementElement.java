package io.github.itzispyder.clickcrystals.gui_beta.elements.client;

import io.github.itzispyder.clickcrystals.data.announce.Announcement;
import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class AnnouncementElement extends GuiElement {

    private final Announcement announcement;

    public AnnouncementElement(Announcement announcement, int x, int y) {
        super(x, y, 285, 30);
        this.announcement = announcement;

        int caret = y + 28;
        caret += StringUtils.wrapLines(announcement.getDesc(), 60, true).size() * 8;

        for (Announcement.Field field : announcement.getFields()) {
            for (String line : StringUtils.wrapLines(field.getDesc(), 45, true)) {
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
        RenderUtils.drawText(context, announcement.getTitle(), x + 5, caret, 0.8F, false);
        caret += 8;
        RenderUtils.drawHorizontalLine(context, x + 5, caret, width - 10, 1, Gray.GRAY.argb);

        for (String line : StringUtils.wrapLines(announcement.getDesc(), 60, true)) {
            caret += 8;
            RenderUtils.drawText(context, line, x + 5, caret, 0.7F, false);
        }
        caret += 10;
        RenderUtils.drawHorizontalLine(context, x + 5, caret, width / 10 * 8, 1, Gray.GRAY.argb);
        caret += 5;

        for (Announcement.Field field : announcement.getFields()) {
            caret += 5;
            RenderUtils.drawText(context, field.getTitle(), x + 15, caret, 0.7F, false);
            caret += 2;
            for (String line : StringUtils.wrapLines(field.getDesc(), 45, true)) {
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
