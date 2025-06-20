package io.github.itzispyder.clickcrystals.gui.screens.modulescreen;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ButtonElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.screens.scripts.ClickScriptIDE;
import io.github.itzispyder.clickcrystals.gui.screens.scripts.DownloadScriptScreen;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class ScriptsBrowsingScreen extends BrowsingScreen {

    public static final String PATH_SCRIPTS = Config.PATH_SCRIPTS
            .replaceFirst("/$", "")
            .replace('/', '\\');

    public static String parentFolder = PATH_SCRIPTS;
    private final ButtonElement backButton;

    public static boolean isRootFolder() {
        return PATH_SCRIPTS.equals(parentFolder);
    }

    public ScriptsBrowsingScreen() {
        super();
        filter();

        this.backButton = new ButtonElement("< Back", baseX + baseWidth - 50 - 10, baseY + 10, 50, 15, (mx, my, self) -> {
            File parent = new File(parentFolder).getParentFile();
            parentFolder = parent.getPath();
            mc.execute(() -> mc.setScreen(new ScriptsBrowsingScreen()));
            this.removeChild(self);
        });
        updateButtonDisplay();
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        // default base
        this.renderDefaultBase(context);

        // title
        String titleName = currentCategory.name();
        if (!isRootFolder())
            titleName += parentFolder.replace('\\', '/')
                    .replaceAll(PATH_SCRIPTS.replace('\\', '/'), "")
                    .replaceAll("/", " §7\\\\§f ");

        // content
        int caret = contentY + 10;
        RenderUtils.drawTexture(context, currentCategory.texture(), contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, titleName, contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorLine(context, contentX, caret, 300, Shades.BLACK);
    }

    @Override
    public void filterByCategory(Category category) {
        filter();
    }

    private void filter() {
        grid.clearPanel();
        grid.clear();
        grid.addEntry(new ScriptDetails(0, 0));
        grid.addEntry(new ScriptDownload(0, 0));
        grid.addEntry(new ScriptDocumentation(0, 0));
        grid.addEntry(new ScriptCreateNew(0, 0));

        List<ScriptedModule> list = system.scriptedModules().values().stream()
                .filter(m -> m.parentFolder.equals(ScriptsBrowsingScreen.parentFolder))
                .sorted(Comparator.comparing(ScriptedModule::getId))
                .toList();

        File parentFile = new File(parentFolder);
        File[] subFiles = parentFile.listFiles();
        if (subFiles != null)
            for (File sub : subFiles)
                if (isValidChildDirectory(sub))
                    grid.addEntry(new FolderElement(sub.getPath(), 0, 0));

        for (Module module : list) {
            ModuleElement e = new ModuleElement(module, 0, 0);
            grid.addEntry(e);
        }
        grid.organize();
        grid.addAllToPanel();
    }

    private boolean isValidChildDirectory(File file) {
        if (file == null || !file.isDirectory())
            return false;

        File[] children = file.listFiles();
        boolean childrenValid = (children != null && children.length > 0);
        boolean scriptLoaded = system.scriptedModules().values().stream().anyMatch(m -> m.parentFolder.equals(file.getPath()));

        if (scriptLoaded)
            return true;
        return childrenValid;
    }

    private void updateButtonDisplay() {
        if (isRootFolder())
            this.removeChild(backButton);
        else if (!getChildren().contains(backButton))
            this.addChild(backButton);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new ScriptsBrowsingScreen());
    }

    protected class FolderElement extends ModuleElement {

        private final String name, path, details;

        public FolderElement(String path, int x, int y) {
            super(null, x, y);
            this.name = "§f" + path.replaceAll("\\\\?(.*\\\\)+", "");
            this.path = path;
            
            this.details = details();
        }
        
        private String details() {
            File thisFolder = new File(path);
            File[] children = thisFolder.listFiles();
            int childrenDir = 0;
            int childrenFiles = 0;

            if (children == null)
                return "Empty";
            for (File child: children) {
                if (child.isDirectory())
                    childrenDir++;
                else if (child.getPath().endsWith(".ccs") || child.getPath().endsWith(".txt"))
                    childrenFiles++;
            }

            String detailsDir = childrenDir + " Folder" + (childrenDir > 1 ? "s" : "");
            String detailsFiles = childrenFiles + " Script" + (childrenFiles > 1 ? "s" : "");

            if (childrenDir > 0 && childrenFiles > 0)
                return detailsDir + ", " + detailsFiles;
            else if (childrenDir == 0 && childrenFiles > 0)
                return detailsFiles;
            else if (childrenDir > 0 && childrenFiles == 0)
                return detailsDir;
            else
                return "Empty";
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            Animator animator = this.getAnimator();
            boolean isAnimating = animator != null && !animator.isFinished();
            if (isAnimating) {
                context.getMatrices().push();
                context.getMatrices().translate(-(float)(width * 0.5 * animator.getAnimationReversed()), 0, 0);
            }
            else {
                this.setAnimator(null);
            }

            if (isHovered(mouseX, mouseY))
                RenderUtils.fillRect(context, x, y, width, height, 0x60FFFFFF);
            RenderUtils.drawTexture(context, Tex.Icons.FOLDER, x + 15, y + 2, 10, 10);
            RenderUtils.drawText(context, name, x + 30, y + height / 3, 0.7F, false);
            RenderUtils.drawRightText(context, "§7" + this.details, x + width - 20, y + height / 3, 0.7F, false);

            if (isAnimating) {
                context.getMatrices().pop();
            }
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            if (button == 0) {
                parentFolder = path;
                filter();
                updateButtonDisplay();
            }
        }
    }

    private static class ScriptDownload extends ModuleElement {
        public ScriptDownload(int x, int y) {
            super(null, x, y);
            this.setTooltip("§7Click to browse pre-made that you can download.");
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x6000B7FF);
            }

            String text = "Need pre-made scripts? §bDownload here ->";
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
//            mc.setScreen(new DownloadScriptScreenOld());
            mc.setScreen(new DownloadScriptScreen());
        }
    }

    private static class ScriptDocumentation extends ModuleElement {
        public ScriptDocumentation(int x, int y) {
            super(null, x, y);
            this.setTooltip("§7Click to view scripting documentation.");
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x6000B7FF);
            }

            String text = "§7Read the full Scripting Documentation and Wiki §bHere ->";
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            system.openUrl("https://bit.ly/ccs-wiki");
        }
    }

    private static class ScriptDetails extends ModuleElement {
        public ScriptDetails(int x, int y) {
            super(null, x, y);
            super.setTooltip(null);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            String text = "§7To manually reload scripts, execute chat command §f%sreload".formatted(ClickCrystals.commandPrefix.getKeyName());
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {

        }
    }

    private static class ScriptCreateNew extends ModuleElement {
        private final SearchBarElement textField = new SearchBarElement(0, 0) {
            private static final String newModule = """
                    def module %s
                    def desc "Custom Scripted Module"
                    
                    on module_enable {
                        
                    }
                    
                    on module_disable {
                        
                    }
                    """;

            {
                this.setDefaultText("§7Enter module name");
            }

            @Override
            public void onKey(int key, int scancode) {
                super.onKey(key, scancode);
                if (!(mc.currentScreen instanceof GuiScreen screen)) {
                    return;
                }

                if (key != GLFW.GLFW_KEY_ENTER) {
                    return;
                }
                if (getQuery().isEmpty()) {
                    screen.selected = null;
                    return;
                }

                String name = getQuery().trim()
                        .replace(' ', '-')
                        .replaceAll("[^a-zA-Z0-9_-]", "")
                        .toLowerCase();

                File file = new File(parentFolder + "/" + name + ".ccs");
                if (!file.exists()) {
                    String preText = newModule.formatted(name);
                    FileValidationUtils.quickWrite(file, preText);
                }
                mc.setScreen(new ClickScriptIDE(file));
            }
        };

        public ScriptCreateNew(int x, int y) {
            super(null, x, y);
            this.setTooltip("§7Type the file name then hit §eENTER§7!");
            this.addChild(textField);
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x60FFFFFF);
            }

            String text = "§7Create new with ClickScript IDE - §eBETA";
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);

            textField.y = y + height / 2 - textField.height / 2;
            textField.x = (int)(x + 30 + (mc.textRenderer.getWidth(text) * 0.7));
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            if (mc.currentScreen instanceof GuiScreen screen) {
                textField.setDefaultText("§c*Enter module name*");
                screen.selected = textField;
            }
        }
    }
}
