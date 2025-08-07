package io.github.itzispyder.clickcrystals.gui.screens.scripts;

import io.github.itzispyder.clickcrystals.commands.commands.ReloadCommand;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ButtonElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.gui.screens.AnimatedBase;
import io.github.itzispyder.clickcrystals.scripting.ScriptFormatter;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.TextUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2f;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DownloadScriptScreen extends AnimatedBase {

    private final int winWidth = RenderUtils.width();
    private final int winHeight = RenderUtils.height();
    private final int baseWidth = 420;
    private final int baseHeight = 240;
    private final int baseX = (winWidth - baseWidth) / 2;
    private final int baseY = (winHeight - baseHeight) / 2;

    private ScriptFilter currentFilter = ScriptFilter.ALL;
    private final SearchBarElement searchbar;
    private final GridOrganizer grid;
    private final LoadingIconElement loading;
    private final List<ScriptObject> cache;

    public DownloadScriptScreen() {
        super("Download Scripts Screen");

        int cellWidth = 40;
        int count = ScriptFilter.values().length;
        int startX = baseX + (baseWidth - ((cellWidth + 3) * count)) / 2;

        GridOrganizer grid = new GridOrganizer(startX, baseY + 30, cellWidth, 10, count, 3);
        for (ScriptFilter filter : ScriptFilter.values()) {
            grid.addEntry(new FilterButton(0, 0, cellWidth, filter));
        }
        grid.organize();
        grid.getEntries().forEach(this::addChild);

        this.loading = new LoadingIconElement(baseX + baseWidth / 2, baseY + baseHeight / 2, 20);
        this.loading.setRendering(false);
        this.addChild(loading);

        this.searchbar = new SearchBarElement(baseX + 10, 0, baseWidth - 20);
        this.searchbar.keyPressCallbacks.add((key, click, scancode, modifiers) -> filterQuery(false));
        this.addChild(this.searchbar);

        this.grid = new GridOrganizer(baseX + 10, baseY + 85, 120, 120, 3, 10);
        this.grid.createPanel(this, baseHeight - 85 - 10);
        this.cache = new ArrayList<>();
        this.addChild(this.grid.getPanel());
        this.filterQuery(true);

        ButtonElement backButton = new ButtonElement("< Back", baseX + baseWidth - 50 - 10, baseY + 10, 50, 15, (mx, my, self) -> UserInputListener.openModulesScreen());
        this.addChild(backButton);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        renderOpaqueBackground(context);

        // backdrop
        RenderUtils.fillRoundRect(context, baseX, baseY, baseWidth, baseHeight, 10, 0xFF202020);
        RenderUtils.fillRoundShadow(context, baseX, baseY, baseWidth, baseHeight, 10, 1, 0xFF00B7FF, 0xFF00B7FF);
        RenderUtils.fillRoundShadow(context, baseX, baseY, baseWidth, baseHeight, 10, 10, 0x8000B7FF, 0x0000B7FF);

        // content
        // navbar
        int caret = baseY;
        int margin = baseX;

        RenderUtils.fillRoundTabTop(context, margin, caret, baseWidth, 50, 10, 0xFF323232);
        caret += 15;
        this.renderTitle(context, caret);
        caret += 45;
        this.searchbar.y = caret;
    }

    public void renderTitle(DrawContext context, int caret) {
        String titleText = "ClickCrystals Scripting Archive";
        int textW = mc.textRenderer.getWidth(titleText) + 20;
        int textX = baseX + (baseWidth - textW) / 2;

        Matrix3x2f matrices = context.getMatrices().pushMatrix();
//        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-12), textX, caret, 0);
        matrices.rotateAbout((float)Math.toRadians(-12), textX, caret);
        RenderUtils.drawTexture(context, Tex.ICON, textX - 12, caret - 7, 20, 20);
        context.getMatrices().popMatrix();

        RenderUtils.drawText(context, titleText, textX + 10, caret, false);
    }

    public synchronized void filterQuery(boolean fetch) {
        if (loading.isRendering())
            return;

        loading.setRendering(true);
        getChildren().removeIf(grid.getEntries()::contains);
        grid.clear();
        grid.clearPanel();

        CompletableFuture.runAsync(() -> {
            String query = searchbar.getLowercaseQuery();
            List<ScriptObject> scripts = cache;

            if (fetch) {
                scripts = ScriptObject.download(currentFilter);
                cache.clear();
            }

            for (ScriptObject script : scripts) {
                if (!script.getQuery().contains(query))
                    continue;
                if (fetch)
                    cache.add(script);
                grid.addEntry(new ScriptDisplay(0, 0, script));
            }
        }).thenRun(() -> {
            grid.organize();
            grid.addAllToPanel();
            grid.getPanel().recalculatePositions();
            loading.setRendering(false);
        });
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new DownloadScriptScreen());
    }

    public class FilterButton extends GuiElement {
        private final ScriptFilter filter;
        private final String name;
        private final Animator animator = new PollingAnimator(300, () -> {
            var c = InteractionUtils.getCursor();
            return isHovered(c.x, c.y);
        });

        public FilterButton(int x, int y, int w, ScriptFilter filter) {
            super(x, y, w, 0);
            this.filter = filter;
            this.name = StringUtils.capitalizeWords(filter.name());
            this.setHeight(2 + mc.textRenderer.fontHeight + 2);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            int fill = Color.WHITE.getHexCustomAlpha(0.5 * animator.getAnimation());
            RenderUtils.fillRoundRect(context, x, y, width, height, 3, fill);
            RenderUtils.drawCenteredText(context, name, x + width / 2, y + (height - 6) / 2, 0.9F, false);

            if (currentFilter == filter) {
                RenderUtils.drawHorLine(context, x, y + height, width, Shades.GENERIC);
            }
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            super.onClick(mouseX, mouseY, button);
            currentFilter = filter;
            filterQuery(true);
        }

        public ScriptFilter getFilter() {
            return filter;
        }
    }

    public enum ScriptFilter {
        ALL,
        ANCHOR,
        CRYSTAL,
        HACKS,
        MACROS,
        TOTEM;

        public String getURL() {
            return "https://itzispyder.github.io/clickcrystals/scripts/content/%s.category".formatted(this.name().toLowerCase());
        }
    }

    public static class ScriptObject {
        private String name, desc, contents, author;

        public ScriptObject(String name, String desc, String contents, String author) {
            this.name = name;
            this.desc = desc;
            this.contents = contents;
            this.author = author;
        }

        public static ScriptObject parse(String script) {
            List<String> lines = script.lines().filter(s -> s.matches(".*(def|module|desc|\\/{2}).*")).toList();
            String name = "Unnamed Module";
            String desc = "No description provided";
            String author = null;
            boolean hasName = false;
            boolean hasDesc = false;

            for (String line : lines) {
                if (hasDesc && hasName)
                    break;

                if (line.matches(".*(def module|module create).*")) {
                    line = line.replaceAll(".*(def module|module create)\\s+", "");
                    line = StringUtils.capitalizeWords(line);
                    name = line;
                    hasName = true;
                }
                else if (line.matches(".*(def description|def desc|description|desc).*")) {
                    line = line.replaceAll(".*(def description|def desc|description|desc)\\s+", "");
                    line = line.replaceAll("(^\\\")|(\\\"$)", "");
                    desc = line;
                    hasDesc = true;
                }
                else if (line.matches("^\\s*(\\/{2})\\s*@\\s*.*$")) {
                    line = line.replaceAll("^\\s*(\\/{2})\\s*@\\s*", "");
                    line = StringUtils.capitalizeWords(line);
                    author = line;
                }
            }
            return new ScriptObject(name, desc, script, author);
        }

        public static List<ScriptObject> download(ScriptFilter filter) {
            system.printf("downloading '%s' scripts...", filter);
            List<ScriptObject> list = new ArrayList<>();

            if (filter == ScriptFilter.ALL) {
                for (ScriptFilter f : ScriptFilter.values())
                    if (f != ScriptFilter.ALL)
                        list.addAll(download(f));
                return list;
            }

            try {
                URL url = URI.create(filter.getURL()).toURL();
                InputStream is = url.openStream();
                String fileContents = ScriptFormatter.decompress(new String(is.readAllBytes()));
                is.close();

                list.addAll(Arrays.stream(fileContents.split("(^```)|(\\n```)"))
                        .filter(s -> s != null && !s.trim().isEmpty())
                        .map(ScriptObject::parse)
                        .toList());
                return list;
            }
            catch (Exception ex) {
                system.printErrF("An error occurred while downloading online scripts: %s", ex.getMessage());
                return list;
            }
        }

        public String getQuery() {
            return (name + author + desc).toLowerCase();
        }

        public String toLocalPath() {
            return Config.PATH_SCRIPTS + "downloads/" + toFileName() + ".ccs";
        }

        public String toFileName() {
            return name.toLowerCase().replace(' ', '-');
        }

        public void download() {
            try {
                File file = new File(toLocalPath());
                FileValidationUtils.quickWrite(file, contents);
            }
            catch (Exception ex) {
                system.printErrF("An error occurred while trying to download script '%s': %s", toLocalPath(), ex.getMessage());
            }
        }

        public boolean alreadyOwned() {
            return new File(toLocalPath()).exists();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

    public static class ScriptDisplay extends GuiElement {
        private final ScriptObject script;
        private final Animator animator = new PollingAnimator(300, () -> {
            var c = InteractionUtils.getCursor();
            return isHovered(c.x, c.y) && this.getParent().isMouseOver(c.x, c.y);
        });
        private final LoadingIconElement downloadStatus;
        private boolean owned;

        public ScriptDisplay(int x, int y, ScriptObject script) {
            super(x, y, 120, 120);
            this.script = script;
            this.owned = script.alreadyOwned();

            this.downloadStatus = new LoadingIconElement(x + width / 2, y + height / 2, 20);
            this.downloadStatus.setRendering(false);
            this.addChild(downloadStatus);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            int caret = y;
            int margin = x;
            double hoverDelta = owned ? 0.0 : animator.getAnimation();
            int shade1 = Color.lerp(0xFF323232, Shades.GENERIC, hoverDelta);
            int shade2 = Color.lerp(0xFF323232, Shades.GENERIC_LOW, hoverDelta);
            RenderUtils.fillRoundRectGradient(context, margin, caret, width, height, 5,
                    0xFF323232,
                    shade1,
                    0xFF323232,
                    0xFF323232,
                    shade2
            );

            // title and author
            caret += 10;
            margin += 5;
            RenderUtils.drawText(context, script.name, margin, caret, false);
            caret += 10;
            RenderUtils.drawText(context, "§7by " + script.author, margin, caret, 0.8F, false);

            // desc
            margin = x + 5;
            caret += 15;
            for (String line : TextUtils.wordWrap(script.desc, width - 10, 0.8F)) {
                RenderUtils.drawText(context, line, margin, caret, 0.8F, false);
                caret = (int)(caret + mc.textRenderer.fontHeight * 0.8F);
            }

            // content

            caret += 5;
            RenderUtils.fillRoundRect(context, margin, caret, width - 10, height - (caret - y) - 5, 5, 0xFF555555);

            caret += 5;
            margin += 5;
            List<String> lines = TextUtils.wordWrap(script.contents, width - 20, 0.5F);
            int max = Math.min(lines.size(), 50);

            context.getMatrices().pushMatrix();
            context.enableScissor(margin, caret, margin + width - 10, y + height - 5);
            for (int i = 0; i < max; i++) {
                String line = lines.get(i);
                RenderUtils.drawText(context, line, margin, caret, 0.4F, false);
                caret = (int)(caret + mc.textRenderer.fontHeight * 0.4F);
            }
            context.disableScissor();
            context.getMatrices().popMatrix();

            RenderUtils.fillVerticalGradient(context, x, y + height - 5 - 20, width, 20, 0x00323232, 0xFF323232);

            // status
            this.renderStatus(context);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            super.onClick(mouseX, mouseY, button);

            if (button != 0 || downloadStatus.isRendering() || owned)
                return;

            CompletableFuture.runAsync(() -> {
                downloadStatus.setX(x + width / 2);
                downloadStatus.setY(y + height / 2);
                downloadStatus.setRendering(true);
                script.download();
                ReloadCommand.reload();
            }).thenRun(() -> {
                downloadStatus.setRendering(false);
                owned = true;
            });
        }

        public void renderStatus(DrawContext context) {
            int caret = y + height - 15;
            int margin = x + width - 15;
            var tex = owned ? Tex.Icons.RESET : Tex.Icons.DOWNLOAD;
            String text = owned ? "§7Already owned" : "§bDownload";
            RenderUtils.drawTexture(context, tex, margin, caret, 10, 10);
            RenderUtils.drawRightText(context, text, margin - 2, caret + 2, 0.8F, false);
        }
    }
}
