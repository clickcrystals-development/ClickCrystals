package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ButtonElement;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import java.util.List;

public class WelcomeScreen extends DefaultBase {

    private final String OFFICIAL_SITE_URL = "https://clickcrystals.xyz/";
    private final String YT_VIDEO_URL = "https://youtu.be/Exuw50Sa20?si=lvNIDHhSxuFVAPhi";
    private final String CCS_WIKI = "https://bit.ly/ccs-wiki";

    public final int windowWidth = mc.getWindow().getScaledWidth();
    public final int windowHeight = mc.getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = windowWidth / 2 - baseWidth / 2;
    public final int baseY = windowHeight / 2 - baseHeight / 2;

    private final ButtonElement siteButton = new ButtonElement(
            "Visit Official Site", 0, 0, 180, 25,
            (mx, my, self) -> system.openUrl(OFFICIAL_SITE_URL)
    );


    private final ButtonElement videoButton = new ButtonElement(
            "ClickCrystals Tutorial", 0, 0, 180, 25,
            (mx, my, self) -> system.openUrl(YT_VIDEO_URL)
    );

    private final ButtonElement wikiButton = new ButtonElement(
            "CSS Wiki", 0, 0, 180, 25,
            (mx, my, self) -> system.openUrl(CCS_WIKI)
    );

    private final ButtonElement closeButton = new ButtonElement(
            "Close", 0, 0, 180, 25,
            (mx, my, self) -> mc.setScreen(null)
    );

    public WelcomeScreen() {
        super("clickcrystals-welcome-screen");
        this.addChild(siteButton);
        this.addChild(videoButton);
        this.addChild(wikiButton);
        this.addChild(closeButton);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderDefaultBase(context);

        int cX = baseX + baseWidth / 2 + 48;
        int cY = baseY + 30;

        String header = StringUtils.color("&bWelcome to ClickCrystals!");
        RenderUtils.drawDefaultCenteredScaledText(context, Text.literal(header), cX, cY, 1.3F, true);
        cY += 30;

        String line1 = StringUtils.color("&7ClickCrystals is a powerful Crystal PvP utility mod,");
        String line2 = StringUtils.color("&7designed to be user-friendly, powerful and fast.");
        List<OrderedText> lines1 = mc.textRenderer.wrapLines(StringVisitable.plain(line1), baseWidth - 40);
        List<OrderedText> lines2 = mc.textRenderer.wrapLines(StringVisitable.plain(line2), baseWidth - 40);

        for (OrderedText line : lines1) {
            context.drawCenteredTextWithShadow(mc.textRenderer, line, cX, cY, 0xFFFFFFFF);
            cY += 14;
        }
        for (OrderedText line : lines2) {
            context.drawCenteredTextWithShadow(mc.textRenderer, line, cX, cY, 0xFFFFFFFF);
            cY += 14;
        }

        int spacing = 28;
        cY += 5; // Padding between text and buttons

        siteButton.x = cX - siteButton.width / 2;
        siteButton.y = cY + 20;

        videoButton.x = cX - videoButton.width / 2;
        videoButton.y = siteButton.y + spacing;

        wikiButton.x = cX - wikiButton.width / 2;
        wikiButton.y = videoButton.y + spacing;

        closeButton.x = cX - closeButton.width / 2;
        closeButton.y = wikiButton.y + spacing;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new WelcomeScreen());
    }
}
