package io.github.itzispyder.clickcrystals.gui.screens.profiles;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.AnimatedBase;
import io.github.itzispyder.clickcrystals.util.minecraft.TextUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.concurrent.CompletableFuture;

import static io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils.*;

public class DownloadProfileScreen extends AnimatedBase {

    private final int winWidth = RenderUtils.width();
    private final int winHeight = RenderUtils.height();
    private final int baseWidth = 420;
    private final int baseHeight = 240;
    private final int baseX = (winWidth - baseWidth) / 2;
    private final int baseY = (winHeight - baseHeight) / 2;
    private ProfileInfo info;
    private final SearchBarElement searchbar;
    private final GridOrganizer panel;

    public DownloadProfileScreen() {
        super("Download Profile Screen");

        LoadingIconElement loadingIcon = new LoadingIconElement(baseX + baseWidth / 2, baseY + baseHeight / 2, 20);
        this.addChild(loadingIcon);
        this.searchbar = new SearchBarElement(baseX + 15, baseY + 35, 300);
        this.searchbar.keyPressCallbacks.add((key, click, scancode, modifiers) -> this.filterQuery());
        this.addChild(searchbar);
        this.addChild(AbstractElement.create()
                .pos(baseX + 15 + 300 + 10, baseY + 35)
                .dimensions(baseWidth - 340, 12)
                .onRender((context, mx, my, b) -> {
                    boolean bl = b.isHovered(mx, my);
                    int r = b.height / 2;
                    int c1 = 0xFF00B7FF;
                    int c2 = 0x0000B7FF;
                    if (bl)
                        fillRoundShadow(context, b.x, b.y, b.width, b.height, r, 5, c1, c2);
                    fillRoundRect(context, b.x, b.y, b.width, b.height, r, c1);
                    drawCenteredText(context, "<- Go Back", b.x + b.width / 2, b.y + (b.height - 7) / 2, false);
                })
                .onPress(button -> mc.execute(() -> mc.setScreen(new ProfilesScreen())))
                .build()
        );

        CompletableFuture<Void> f = ProfileInfo.request();
        this.panel = new GridOrganizer(baseX + 15, baseY + 60, 120, 120, 3, 10);
        int panelWidth = baseWidth - 15;
        int panelHeight = baseHeight - 60;

        f.thenRun(() -> {
            if (f.isDone() && ProfileInfo.hasCurrent()) {
                this.info = ProfileInfo.getCurrent();
            }
            else {
                this.info = ProfileInfo.createNull();
            }
            if (info.configs().length == 0) {
                return;
            }

            for (ProfileInfo.Info profile : info.configs()) {
                panel.addEntry(new ProfileElement(profile, 0, 0));
            }

            panel.organize();
            panel.createPanel(this, panelHeight);
            panel.addAllToPanel();
            loadingIcon.setRendering(false);
            this.addChild(panel.getPanel());
        });
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderOpaqueBackground(context);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(baseX, baseY);

        // backdrop
        RenderUtils.fillRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Shades.TRANS_BLACK);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 1, 0xFF00B7FF, 0xFF00B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, -10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 10, 0x8000B7FF, 0x0000B7FF);

        int margin = 15;
        int caret = 10;
        drawTexture(context, Tex.Icons.SETTINGS, margin, caret, 20, 20);
        margin += 25;
        caret += 6;
        drawText(context, "Online Configuration Profiles", margin, caret, 1.3F, false);


        context.getMatrices().popMatrix();
    }

    private void filterQuery() {
        panel.clearPanel();
        panel.clear();

        String query = searchbar.getLowercaseQuery();
        for (ProfileInfo.Info profile : info.configs()) {
            String pq = "%s:%s:%s".formatted(profile.name(), profile.desc(), profile.contents()).toLowerCase();
            if (pq.contains(query))
                panel.addEntry(new ProfileElement(profile, 0, 0));
        }

        panel.organize();
        panel.addAllToPanel();
    }

    private class ProfileElement extends GuiElement {
        private final LoadingIconElement loading;
        private final ProfileInfo.Info profile;
        private boolean owned;

        public ProfileElement(ProfileInfo.Info profile, int x, int y) {
            super(x, y, 120, 120);
            this.profile = profile;
            this.owned = info.alreadyOwns(profile);
            this.loading = new LoadingIconElement(0, 0, 20);
            this.loading.setRendering(false);
            this.addChild(loading);
        }

        @Override
        public void onRender(DrawContext context, int mx, int my) {
            int bg1 = Shades.TRANS_GRAY;
            int bg2 = Shades.TRANS_GENERIC_LOW;
            int bg3 = Shades.TRANS_GENERIC;

            if (isHovered(mx, my) && !loading.isRendering())
                fillRoundRectGradient(context, x, y, width, height, 3, bg1, bg3, bg1, bg2, bg2);
            else
                fillRoundRect(context, x, y, width, height, 3, bg1);

            int caret = y + 10;
            int margin = x + 5;
            String prefix = owned ? "§7" : "§b";
            for (String line : TextUtils.wordWrap(profile.name(), width - 5 - 5, 1F)) {
                drawText(context, prefix + line, margin, caret, false);
                caret += 10;
            }
            caret += 5;
            for (String line : TextUtils.wordWrap(profile.desc(), width - 5 - 5, 0.8F)) {
                drawText(context, "§7" + line, margin, caret, 0.8F, false);
                caret += 8;
            }

            caret = y + height - 15;
            margin = x + width - 15;
            var tex = owned ? Tex.Icons.RESET : Tex.Icons.DOWNLOAD;
            String text = owned ? "§7Already owned" : "§bDownload";
            drawTexture(context, tex, margin, caret, 10, 10);
            drawRightText(context, text, margin - 2, caret + 2, 0.8F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            super.onClick(mouseX, mouseY, button);

            if (button != 0 || loading.isRendering() || owned)
                return;

            CompletableFuture.runAsync(() -> {
                loading.setX(x + width / 2);
                loading.setY(y + height / 2);
                loading.setRendering(true);
                info.download(profile);
            }).thenRun(() -> {
                loading.setRendering(false);
                owned = true;
            });
        }
    }
}
