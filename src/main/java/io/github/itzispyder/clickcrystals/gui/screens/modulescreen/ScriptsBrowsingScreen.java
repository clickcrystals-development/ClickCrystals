package io.github.itzispyder.clickcrystals.gui.screens.modulescreen;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.ModuleElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
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
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptsBrowsingScreen extends BrowsingScreen {

    public static final String PATH_SCRIPTS;
    public static String parentFolder;

    static {
        String pathScripts = Config.PATH_SCRIPTS;
        pathScripts = pathScripts.replace('/', File.separatorChar);
        if (pathScripts.endsWith(File.separator))
            pathScripts = pathScripts.substring(0, pathScripts.length() - 1);

        PATH_SCRIPTS = pathScripts;
        parentFolder = PATH_SCRIPTS;
    }


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
            titleName += parentFolder.replace(PATH_SCRIPTS, "")
                    .replace(File.separator, " §7\\§f ");

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
            this.path = path;
            this.name = new File(path).getName();
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
                context.getMatrices().pushMatrix();
                context.getMatrices().translate(-(float)(width * 0.5 * animator.getAnimationReversed()), 0);
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
                context.getMatrices().popMatrix();
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
        private static final String newModule = """
                    def module %s
                    def desc "Custom Scripted Module"
                    
                    on module_enable {
                        
                    }
                    
                    on module_disable {
                        
                    }
                    """;

        private final SearchBarElement textField = new SearchBarElement(0, 0) {
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

                createScript(name);
            }
        };
        private final AbstractElement pasteScriptButton;

        public ScriptCreateNew(int x, int y) {
            super(null, x, y);
            this.setTooltip("§7Type the file name then hit §eENTER§7!");
            this.pasteScriptButton = AbstractElement.create().pos(0, 0).dimensions(40, textField.height)
                    .onRender(AbstractElement.RENDER_BUTTON.apply(() -> "Paste"))
                    .onPress(self -> {
                        String script = mc.keyboard.getClipboard();
                        Notification notification = Notification.create()
                                .ccsIcon()
                                .id("invalid-clipboard-script")
                                .title("Invalid Clipboard Script")
                                .text("§eYour clipboard was invalid. Please copy a script first!")
                                .stayTime(1000 * 3)
                                .build();

                        if (script == null || script.isBlank()) {
                            if (PlayerUtils.valid()) {
                                system.closeCurrentScreen();
                                notification.sendToClient();
                            }
                            return;
                        }

                        Pattern pattern = Pattern.compile(".*(define module|def module|module create) (\\S*).*", Pattern.DOTALL);
                        Matcher matcher = pattern.matcher(script);

                        if (!matcher.matches()) {
                            if (PlayerUtils.valid()) {
                                system.closeCurrentScreen();
                                notification.sendToClient();
                            }
                            return;
                        }

                        String name = matcher.group(2);
                        ScriptCreateNew.createScriptWithPretext(name, script);
                    })
                    .build();
            this.pasteScriptButton.setTooltip("Paste script from clipboard");
            this.addChild(pasteScriptButton);
            this.addChild(textField);
        }

        public static void createScript(String moduleId) {
            File file = new File(parentFolder + File.separator + moduleId + ".ccs");
            if (!file.exists()) {
                String preText = newModule.formatted(moduleId);
                FileValidationUtils.quickWrite(file, preText);
            }
            mc.setScreen(new ClickScriptIDE(file));
        }

        public static void createScriptWithPretext(String moduleId, String pretext) {
            File file = new File(parentFolder + File.separator + moduleId + ".ccs");
            if (!file.exists())
                FileValidationUtils.quickWrite(file, pretext);
            mc.setScreen(new ClickScriptIDE(file));
        }

        @Override
        public void onRender(DrawContext context, int mouseX, int mouseY) {
            if (isHovered(mouseX, mouseY)) {
                RenderUtils.fillRect(context, x, y, width, height, 0x60FFFFFF);
            }

            String text = "§7Create new with ClickScript IDE - §eBETA";
            RenderUtils.drawText(context, text, x + 10, y + height / 3, 0.7F, false);

            int margin = (int)(x + 20 + (mc.textRenderer.getWidth(text) * 0.7));

            textField.y = y + height / 2 - textField.height / 2;
            textField.x = margin;
            margin += textField.width + 5;

            pasteScriptButton.x = margin;
            pasteScriptButton.y = textField.y;
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