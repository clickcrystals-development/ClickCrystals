package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.client.client.ClickCrystalsGate;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsInfo;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.HyperLinkElement;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public class BanScreen extends GuiScreen {

    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    private final ClickCrystalsGate gate = new ClickCrystalsGate();
    private final HyperLinkElement appealLink = new HyperLinkElement(0, 0, "https://discord.gg/tMaShNzNtP", 1.0F);

    public BanScreen() {
        super("ban-screen");
        this.addChild(appealLink);
        system.printErrF("You have been banished! Ban ID: {\"name\": \"%s\", \"id\": \"%s\"}".formatted(user().get().name(), user().get().id()));
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        if (PlayerUtils.invalid()) {
            this.renderPanoramaBackground(context, delta);
        }
        this.applyBlur(context);
        this.renderDarkening(context);
        //RenderUtils.fillGradient(context, 0, 0, windowWidth, windowHeight, 0xA0000000, 0xA0FF4538);

        int cX = baseX + baseWidth / 2;
        int cY = baseY + baseHeight / 6;
        var user = user().get();
        var ses = gate.getBanSession();
        String text;

        text = StringUtils.color("&cMinecraft Account");
        RenderUtils.drawDefaultCenteredScaledText(context, Text.literal(text), cX, cY += 10, 1.0F, true);
        text = StringUtils.color("&7{\"name\": \"%s\", \"id\": \"%s\"}".formatted(user.name(), user.id()));
        RenderUtils.drawDefaultCenteredScaledText(context, Text.literal(text), cX, cY += 10, 1.0F, true);
        text = StringUtils.color("&cis blacklisted from ClickCrystals!");
        RenderUtils.drawDefaultCenteredScaledText(context, Text.literal(text), cX, cY += 10, 1.0F, true);

        cY += 20;
        text = StringUtils.color("&cReason:\n&7%s".formatted(ses.isPresent() ? ses.get().reason() : "Unspecified reason."));
        var lines = mc.textRenderer.wrapLines(StringVisitable.plain(text), baseWidth);
        for (OrderedText line : lines) {
            context.drawCenteredTextWithShadow(mc.textRenderer, line, cX, cY, 0xFFFFFFFF);
            cY += 10;
        }

        cY += 20;
        text = StringUtils.color("&cAppeal at:");
        RenderUtils.drawDefaultCenteredScaledText(context, Text.literal(text), cX, cY += 10, 1.0F, true);
        appealLink.x = cX - appealLink.width / 2;
        appealLink.y = cY + 10;
    }

    public Voidable<ClickCrystalsInfo.User> user() {
        return gate.getSessionUser();
    }

    @Override
    public void close() {

    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new BanScreen());
    }
}
