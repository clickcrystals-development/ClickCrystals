package io.github.itzispyder.clickcrystals.gui.screens.settings;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.client.ClickCrystalsGate;
import io.github.itzispyder.clickcrystals.data.announce.Announcement;
import io.github.itzispyder.clickcrystals.data.announce.BulletinBoard;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.AnnouncementElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class InfoScreen extends DefaultBase {

    private static final String banTitlePass = "&aYou are clear!";
    private static final String banTitleFail = "&cYou are blacklisted!";
    private static final String banDescPass = "Your session user is approved by ClickCrystals.";
    private static final String banDescFail = "Your session user is blacklisted from ClickCrystals, your game will close in a few moments.";
    private boolean isBanned;
    private static final String updateTitlePass = "&aYou are UP-TO-DATE!";
    private static final String updateTitleFail = "&cUpdate available!";
    private static final String updateDescPass = "You are on the latest version of ClickCrystals.";
    private static final String updateDescFail = "You are a bit behind on updates, update now!";
    private static final String versionDesc = "&7The latest version is %s, &7you are on %s";
    private boolean isLatest;

    public InfoScreen() {
        super("Bulletin Screen");

        ScrollPanelElement panel = new ScrollPanelElement(this, contentX + 5, contentY + 21, contentWidth - 5, contentHeight - 21);
        LoadingIconElement loadingIcon = new LoadingIconElement(contentX + contentWidth / 2 - 10, contentY + contentHeight / 2 - 10, 20);

        BulletinBoard.request().thenRun(() -> {
            ClickCrystalsGate gate = new ClickCrystalsGate();
            isBanned = gate.isBanned();
            isLatest = ClickCrystals.matchLatestVersion();

            if (isBanned) {
                system.scheduler.runDelayedTask(gate::banishCurrentSession, 3000);
            }

            initPanel(panel);
            loadingIcon.setRendering(false);
        });

        this.addChild(loadingIcon);
        this.addChild(panel);
    }

    private void initPanel(ScrollPanelElement panel) {
        int caret = contentY + 25;
        int margin = contentX + 5;

        Announcement.Field fBan = new Announcement.Field(isBanned ? banTitleFail : banTitlePass, isBanned ? banDescFail : banDescPass);
        Announcement aBan = new Announcement("Am I blacklisted?", "Hey, you'll never know", fBan);
        AnnouncementElement aeBan = new AnnouncementElement(aBan, margin, caret);
        panel.addChild(aeBan);
        caret += aeBan.height + 5;

        Announcement.Field fMotd = new Announcement.Field("MOTD", StringUtils.decolor(ClickCrystals.info.getMotd()));
        Announcement.Field fUpdate = new Announcement.Field(isLatest ? updateTitlePass : updateTitleFail, isLatest ? updateDescPass : updateDescFail);
        Announcement.Field fVersion = new Announcement.Field(StringUtils.format(versionDesc, ClickCrystals.getLatestVersion(), version), "");
        Announcement aMotd = new Announcement("Message of the Day", "Our little daily announcement.", fMotd, fUpdate, fVersion);
        AnnouncementElement aeMotd = new AnnouncementElement(aMotd, margin, caret);
        panel.addChild(aeMotd);
        caret += aeMotd.height + 5;

        Announcement.Field fOwner = new Announcement.Field("&bClient Owners", ClickCrystals.info.namesStringLong(ClickCrystals.info.collectOwners()));
        Announcement.Field fStaff = new Announcement.Field("&bCommunity Staff", ClickCrystals.info.namesStringLong(ClickCrystals.info.collectStaff()));
        Announcement.Field fDonos = new Announcement.Field("&bCommunity Supporters", ClickCrystals.info.namesStringLong(ClickCrystals.info.collectVip()));
        Announcement aTeam = new Announcement("Our Team", "\"We got cool capes!\"",fOwner, fStaff, fDonos);
        AnnouncementElement aeTeam = new AnnouncementElement(aTeam, margin, caret);
        panel.addChild(aeTeam);
        caret += aeTeam.height + 5;
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawText(context, "ClickCrystals Information", contentX + 10, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new InfoScreen());
    }
}
