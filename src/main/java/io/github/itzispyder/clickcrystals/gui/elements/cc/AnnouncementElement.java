package io.github.itzispyder.clickcrystals.gui.elements.cc;

import io.github.itzispyder.clickcrystals.data.announce.Announcement;
import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.TextAlignment;
import io.github.itzispyder.clickcrystals.gui.elements.design.ImageElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.elements.design.TextElement;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class AnnouncementElement extends GuiElement {

    private Announcement announcement;
    private final GuiScreen parentScreen;
    private final ImageElement background;

    public AnnouncementElement(GuiScreen parentScreen, Announcement announcement, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.announcement = announcement;
        this.parentScreen = parentScreen;
        this.background = new ImageElement(GuiTextures.BULLETIN, x, y, width, height);
        this.setAnnouncement(announcement);
    }

    public void setAnnouncement(Announcement announcement) {
        if (announcement == null) return;
        this.announcement = announcement;
        this.clearChildren();
        parentScreen.screenRenderListeners.clear();

        TextElement title = new TextElement(announcement.getTitle(), TextAlignment.LEFT, 0.8F, x + 7, y + 8);
        this.addChild(background);
        this.addChild(title);

        int caret = title.y + 9;
        for (String line : StringUtils.wrapLines(announcement.getDesc(), 60, true)) {
            TextElement desc = new TextElement("§7" + line, TextAlignment.LEFT, 0.6F, title.x, caret);
            this.addChild(desc);
            caret += 6;
        }

        caret += 4;
        TextElement divider = new TextElement("§8§m§l-----------------------------------", TextAlignment.LEFT, 0.8F, x + 7, caret);
        this.addChild(divider);

        caret += 10;
        ScrollPanelElement panel = new ScrollPanelElement(parentScreen, x, caret, width - 10, y + height - 10 - caret);
        this.addChild(panel);

        // announcement fields
        for (Announcement.Field field : announcement.getFields()) {
            TextElement fTitle = new TextElement(field.getTitle(), TextAlignment.LEFT, 0.6F, x + 15, caret);
            panel.addChild(fTitle);
            caret += 7;

            for (String line : StringUtils.wrapLines(field.getDesc(), 50, true)) {
                TextElement desc = new TextElement("§7" + line, TextAlignment.LEFT, 0.5F, fTitle.x, caret);
                panel.addChild(desc);
                caret += 5;
            }
            caret += 10;
        }
    }

    public void setBackground(Identifier texture) {
        background.setTexture(texture);
    }

    public GuiScreen getParentScreen() {
        return parentScreen;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }

    public Announcement getAnnouncement() {
        return announcement;
    }
}
