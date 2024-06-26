package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import static io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils.*;

public class DiscordInviteScreen extends GuiScreen {

    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    private final AbstractElement inviteCC, inviteYSB, decline;
    private final Animator aniCC, aniYSB, aniDecline;

    public DiscordInviteScreen() {
        super("Discord Invitation Screen");

        inviteCC = AbstractElement.create().dimensions(baseWidth / 4, 12)
                .onPress(button -> system.openUrl("https://discord.gg/tMaShNzNtP"))
                .onRender((context, mouseX, mouseY, button) -> {
                    int fill = button.isHovered(mouseX, mouseY) ? Shades.GENERIC_LOW : Shades.GENERIC;
                    fillRoundHoriLine(context, button.x, button.y, baseWidth / 4, 12, fill);
                    drawText(context, "Join ClickCrystals", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        inviteYSB = AbstractElement.create().dimensions(baseWidth / 4, 12)
                .onPress(button -> system.openUrl("https://discord.gg/YcQzv9c3AX"))
                .onRender((context, mouseX, mouseY, button) -> {
                    int fill = button.isHovered(mouseX, mouseY) ? Shades.GENERIC_LOW : Shades.GENERIC;
                    fillRoundHoriLine(context, button.x, button.y, baseWidth / 4, 12, fill);
                    drawText(context, "Join VoxD4pe", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        decline = AbstractElement.create().dimensions(baseWidth / 4, 12)
                .onPress(button -> mc.setScreen(new HomeScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    int fill = button.isHovered(mouseX, mouseY) ? Shades.GRAY : Shades.DARK_GRAY;
                    fillRoundHoriLine(context, button.x, button.y, baseWidth / 4, 12, fill);
                    drawText(context, "§7<- Go Back", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        inviteCC.setRendering(false);
        inviteYSB.setRendering(false);
        decline.setRendering(false);
        this.addChild(inviteCC);
        this.addChild(inviteYSB);
        this.addChild(decline);

        aniCC = new PollingAnimator(100, inviteCC::isRendering);
        aniYSB = new PollingAnimator(100, inviteYSB::isRendering);
        aniDecline = new PollingAnimator(100, decline::isRendering);

        system.scheduler.runChainTask()
                .thenWait(500)
                .thenRun(() -> inviteCC.setRendering(true))
                .thenWait(100)
                .thenRun(() -> inviteYSB.setRendering(true))
                .thenWait(100)
                .thenRun(() -> decline.setRendering(true))
                .thenWait(100)
                .startChain();
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderOpaqueBackground(context);
        drawRoundTexture(context, Tex.Backdrops.BACKDROP_1, baseX, baseY, baseWidth, baseHeight, 10);
        RenderUtils.fillRoundShadow(context, baseX, baseY, baseWidth, baseHeight, 35, 1, 0xFFE860FC, 0xFFE860FC);
        RenderUtils.fillRoundShadow(context, baseX, baseY, baseWidth, baseHeight, 35, 10, 0x80E860FC, 0x00E860FC);

        int caret = baseY + baseHeight / 5 * 3 + 20;
        int margin = baseX + baseWidth / 3 * 2;

        inviteCC.x = margin;
        inviteCC.y = (int)(caret - (-50 * aniCC.getProgressClampedReversed()));
        caret += 18;
        inviteYSB.x = margin;
        inviteYSB.y = (int)(caret - (-50 * aniYSB.getProgressClampedReversed()));
        caret += 18;
        decline.x = (int)(margin - (-50 * aniDecline.getProgressClampedReversed()));
        decline.y = caret;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new DiscordInviteScreen());
    }
}
