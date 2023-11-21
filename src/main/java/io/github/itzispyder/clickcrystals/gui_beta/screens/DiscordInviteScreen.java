package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.gui_beta.GuiScreen;
import io.github.itzispyder.clickcrystals.gui_beta.elements.AbstractElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.gui_beta.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.concurrent.atomic.AtomicInteger;

public class DiscordInviteScreen extends GuiScreen {

    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    private final AbstractElement inviteCC, inviteOgre, decline;
    private final AtomicInteger translationCC, translationOgre, translationDecline;

    public DiscordInviteScreen() {
        super("Discord Invitation Screen");

        inviteCC = AbstractElement.create().dimensions(baseWidth / 4, 12)
                .onPress(button -> system.openUrl("https://discord.gg/tMaShNzNtP"))
                .onRender((context, mouseX, mouseY, button) -> {
                    Gray fill = button.isHovered(mouseX, mouseY) ? Gray.GENERIC_LOW : Gray.GENERIC;
                    RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, baseWidth / 4, 12, fill);
                    RenderUtils.drawText(context, "Join ClickCrystals", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        inviteOgre = AbstractElement.create().dimensions(baseWidth / 4, 12)
                .onPress(button -> system.openUrl("https://discord.gg/ogrenetworks-season-xiv-916101986779299942"))
                .onRender((context, mouseX, mouseY, button) -> {
                    Gray fill = button.isHovered(mouseX, mouseY) ? Gray.GENERIC_LOW : Gray.GENERIC;
                    RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, baseWidth / 4, 12, fill);
                    RenderUtils.drawText(context, "Join OgreDupe", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        decline = AbstractElement.create().dimensions(baseWidth / 4, 12)
                .onPress(button -> mc.setScreen(new HomeScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    Gray fill = button.isHovered(mouseX, mouseY) ? Gray.GRAY : Gray.DARK_GRAY;
                    RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, baseWidth / 4, 12, fill);
                    RenderUtils.drawText(context, "§7<- Go Back", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        inviteCC.setRendering(false);
        inviteOgre.setRendering(false);
        decline.setRendering(false);
        this.addChild(inviteCC);
        this.addChild(inviteOgre);
        this.addChild(decline);

        translationCC = new AtomicInteger(-50);
        translationOgre = new AtomicInteger(-50);
        translationDecline = new AtomicInteger(-50);

        system.scheduler.runChainTask()
                .thenWait(500)
                .thenRun(() -> inviteCC.setRendering(true))
                .thenRepeat(translationCC::getAndIncrement, 2, 50)
                .thenRun(() -> inviteOgre.setRendering(true))
                .thenRepeat(translationOgre::getAndIncrement, 2, 50)
                .thenRun(() -> decline.setRendering(true))
                .thenRepeat(translationDecline::getAndIncrement, 2, 50)
                .startChain();
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderUtils.fillGradient(context, 0, 0, windowWidth, windowHeight, 0xA0F95A70, 0xA06C64D3);
        RenderUtils.drawTexture(context, Tex.Backdrops.BACKDROP_1, baseX, baseY, baseWidth, baseHeight);

        int caret = baseY + baseHeight / 5 * 3 + 20;
        int margin = baseX + baseWidth / 3 * 2;

        inviteCC.x = margin;
        inviteCC.y = caret - translationCC.get();
        caret += 18;
        inviteOgre.x = margin;
        inviteOgre.y = caret - translationOgre.get();
        caret += 18;
        decline.x = margin - translationDecline.get();
        decline.y = caret;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new DiscordInviteScreen());
    }
}
