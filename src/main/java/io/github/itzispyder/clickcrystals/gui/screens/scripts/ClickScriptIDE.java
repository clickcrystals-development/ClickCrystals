package io.github.itzispyder.clickcrystals.gui.screens.scripts;

import io.github.itzispyder.clickcrystals.client.commands.commands.ReloadCommand;
import io.github.itzispyder.clickcrystals.client.system.Config;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.TextFieldElement;
import io.github.itzispyder.clickcrystals.gui.misc.ChatColor;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.ScriptsBrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.components.Conditionals;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.ConfigCmd;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.DefineCmd;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.ModuleCmd;
import io.github.itzispyder.clickcrystals.scripting.syntax.logic.OnEventCmd;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Dimensions;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.platform.InputConstants;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils.*;

public class ClickScriptIDE extends DefaultBase {

    public static final TextFieldElement.TextHighlighter CLICKSCRIPT_HIGHLIGHTER = new TextFieldElement.TextHighlighter() {{
        ChatColor og = getOriginalColor();
        Function<ChatColor, Function<String, String>> applyColor = c -> s -> "%s%s%s".formatted(c, s, og);
        Function<ChatColor, Function<String, String>> applyUnderline = c -> s -> "%s§n%s§r%s".formatted(c, s, og);
        Function<ChatColor, Function<String, String>> applyItalic = c -> s -> "%s§o%s§r%s".formatted(c, s, og);

        this.put(s -> s.contains("\""), applyColor.apply(ChatColor.DARK_GREEN));
        this.put(s -> s.startsWith("@"), applyColor.apply(ChatColor.AQUA));
        this.put(s -> StringUtils.startsWithAny(s, ":", "#"), applyColor.apply(ChatColor.DARK_GREEN));
        this.put(s -> s.replaceAll("[0-9><=!.+~-]", "").isEmpty(), applyColor.apply(ChatColor.DARK_AQUA));
        this.put(ChatColor.GRAY, "then", "back", "all");
        this.put(s -> ArrayUtils.enumContains(OnEventCmd.EventType.class, s), applyUnderline.apply(ChatColor.YELLOW));
        this.put(Conditionals::isRegistered, applyItalic.apply(ChatColor.YELLOW));
        this.put(ChatColor.YELLOW, Arrays.stream(InputType.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(ModuleCmd.Action.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(TargetType.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(ConfigCmd.Type.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(DefineCmd.Type.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(Dimensions.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.ORANGE, ClickScript.collectNames());
        this.comments("//", ChatColor.DARK_GRAY);
    }};

    private static final List<Pair<String, String>> KEYBIND_ENTRIES = List.of(
            Pair.of("Ctrl+S", "Save"),
            Pair.of("Ctrl+Z", "Undo"),
            Pair.of("Ctrl+Y", "Redo"),
            Pair.of("Ctrl+C", "Copy"),
            Pair.of("Ctrl+X", "Cut"),
            Pair.of("Ctrl+V", "Paste"),
            Pair.of("Ctrl+A", "Select All"),
            Pair.of("Ctrl+D", "Duplicate Line"),
            Pair.of("Ctrl+/", "Toggle Comment"),
            Pair.of("Tab", "Autocomplete / Indent"),
            Pair.of("Ctrl+←→", "Word Jump"),
            Pair.of("↑↓", "Navigate Suggestions"),
            Pair.of("Enter", "Insert Suggestion"),
            Pair.of("Escape", "Dismiss")
    );

    private final ClickScriptAutocomplete autocomplete = new ClickScriptAutocomplete();
    private final LoadingIconElement loading;
    private final File currentFile;
    private boolean showKeybinds = false;
    private int kbPx, kbPy, kbPw, kbPh;

    public TextFieldElement textField = new TextFieldElement(contentX, contentY + 21, contentWidth, contentHeight - 21) {{
        this.setHighlighter(CLICKSCRIPT_HIGHLIGHTER);
        this.setBackgroundColor(ChatColor.RESET);
    }};

    private final AbstractElement
            saveButton,
            saveAndCloseButton,
            closeButton,
            discardChangesButton,
            keybindsButton,
            openFileButton,
            openScriptsButton,
            deleteButton;

    public ClickScriptIDE(ScriptedModule module) {
        this(new File(module.filepath));
    }

    public ClickScriptIDE(File file) {
        super("ClickScript IDE");
        this.currentFile = file;
        this.addChild(textField);

        this.loading = new LoadingIconElement(contentX + contentWidth / 2 - 10, contentY + contentHeight / 2 - 10, 20);
        this.loading.setRendering(false);
        this.addChild(loading);
        this.loadContents();

        this.navlistModules.forEach(this::removeChild);
        this.removeChild(buttonSearch);

        textField.setKeyInterceptor(key -> {
            if (key == InputConstants.KEY_ESCAPE && showKeybinds) {
                showKeybinds = false;
                return true;
            }
            if (autocomplete.onKey(key)) return true;
            if (autocomplete.isInsertKey(key)) {
                String sel = autocomplete.getSelected();
                if (sel != null) {
                    textField.insertCompletion(sel);
                    autocomplete.hide();
                    return true;
                }
            }
            if (key == InputConstants.KEY_S && ctrlKeyPressed) {
                saveContents();
                return true;
            }
            return false;
        });

        textField.setOnContentChanged(typed -> {
            if (typed) autocomplete.update(textField.getCurrentLine(), textField.getCursorColInLine());
            else autocomplete.hide();
        });

        this.mouseDragListeners.add((mx, my, button, dx, dy) -> {
            if (button == 0) textField.onDrag(mx, my);
        });

        this.screenRenderListeners.add((context, mouseX, mouseY, delta) -> {
            autocomplete.render(context, textField.getCursorPixelX(), textField.getCursorPixelY(), contentX, contentWidth);
            if (showKeybinds) renderKeybindsTooltip(context);
        });

        saveButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Save contents")
                .onPress(button -> saveContents())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.LIGHT_GRAY);
                    drawText(context, "Save", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        saveAndCloseButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Save contents then close IDE")
                .onPress(button -> saveContents().accept(f -> f.thenRun(UserInputListener::openModulesScreen)))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.LIGHT_GRAY);
                    drawText(context, "Save & Close", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        closeButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Close without saving")
                .onPress(button -> UserInputListener.openModulesScreen())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.GENERIC_LOW);
                    drawText(context, "Close", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        discardChangesButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Undo all modifications")
                .onPress(button -> loadContents())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.GENERIC_LOW);
                    drawText(context, "Discard Changes", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        keybindsButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Show keybind reference")
                .onPress(button -> showKeybinds = !showKeybinds)
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.LIGHT_GRAY);
                    drawText(context, "Keybinds", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        openFileButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Open file in File Explorer")
                .onPress(button -> system.openFile(currentFile.getPath()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.LIGHT_GRAY);
                    drawText(context, "Open .CCS File", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        openScriptsButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Open scripts folder")
                .onPress(button -> system.openFile(Config.PATH_SCRIPTS))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.LIGHT_GRAY);
                    drawText(context, "Open Scripts", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        deleteButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Delete this script (Can't Undo This!)")
                .onPress(button -> deleteScript())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY))
                        fillRoundHoriLine(context, button.x, button.y, navWidth, button.height, Shades.LIGHT_GRAY);
                    drawText(context, "§cDelete File", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        this.addChild(saveButton);
        this.addChild(saveAndCloseButton);
        this.addChild(closeButton);
        this.addChild(discardChangesButton);
        this.addChild(keybindsButton);
        this.addChild(openFileButton);
        this.addChild(openScriptsButton);
        this.addChild(deleteButton);

        this.selected = textField;
    }

    @Override
    public void baseRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        renderOpaqueBackground(context);

        context.pose().pushMatrix();
        context.pose().translate(baseX, baseY);

        fillRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Shades.TRANS_BLACK);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 1, 0xFF00B7FF, 0xFF00B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, -10, 0x8000B7FF, 0x0000B7FF);
        RenderUtils.fillRoundShadow(context, 0, 0, baseWidth, baseHeight, 10, 10, 0x8000B7FF, 0x0000B7FF);
        fillRoundTabTop(context, 110, 10, 300, 230, 10, Shades.DARK_GRAY);

        int caret = 10;
        drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        drawText(context, "ClickCrystals v%s".formatted(version), 22, 11, 0.7F, false);
        caret += 10;
        drawHorLine(context, 10, caret, 90, Shades.GRAY);
        caret += 6;
        buttonHome.x = baseX + 10;
        buttonHome.y = baseY + caret;
        caret += 12;
        buttonModules.x = baseX + 10;
        buttonModules.y = baseY + caret;
        caret += 12;
        buttonNews.x = baseX + 10;
        buttonNews.y = baseY + caret;
        caret += 12;
        buttonSettings.x = baseX + 10;
        buttonSettings.y = baseY + caret;

        caret += 16;
        drawHorLine(context, 10, caret, 90, Shades.GRAY);
        caret += 6;
        saveButton.x = baseX + 10;
        saveButton.y = baseY + caret;
        caret += 16;
        saveAndCloseButton.x = baseX + 10;
        saveAndCloseButton.y = baseY + caret;
        caret += 16;
        closeButton.x = baseX + 10;
        closeButton.y = baseY + caret;
        caret += 16;
        discardChangesButton.x = baseX + 10;
        discardChangesButton.y = baseY + caret;

        caret += 16;
        drawHorLine(context, 10, caret, 90, Shades.GRAY);
        caret += 6;
        keybindsButton.x = baseX + 10;
        keybindsButton.y = baseY + caret;

        caret += 16;
        drawHorLine(context, 10, caret, 90, Shades.GRAY);
        caret += 6;
        openFileButton.x = baseX + 10;
        openFileButton.y = baseY + caret;
        caret += 16;
        openScriptsButton.x = baseX + 10;
        openScriptsButton.y = baseY + caret;
        caret += 16;
        deleteButton.x = baseX + 10;
        deleteButton.y = baseY + caret;

        context.pose().popMatrix();

        int contentCaret = contentY + 10;
        drawTexture(context, Tex.ICON_CLICKSCRIPT, contentX + 10, contentCaret - 7, 15, 15);
        drawText(context, "Editing '%s'".formatted(currentFile.getName()), contentX + 30, contentCaret - 4, false);
        contentCaret += 10;
        drawHorLine(context, contentX, contentCaret, 300, Shades.BLACK);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent click, boolean doubled) {
        if (showKeybinds) {
            double mx = click.x(), my = click.y();
            boolean onClose = mx >= kbPx + kbPw - 18 && mx <= kbPx + kbPw
                    && my >= kbPy && my <= kbPy + 16;
            if (onClose || mx < kbPx || mx > kbPx + kbPw || my < kbPy || my > kbPy + kbPh) {
                showKeybinds = false;
            }
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    private void renderKeybindsTooltip(GuiGraphicsExtractor context) {
        final int TITLE_H = 18;
        final int ROW_H = 10;
        final int PADDING = 6;
        final int CLOSE_W = 14;
        kbPw = 220;
        kbPh = TITLE_H + KEYBIND_ENTRIES.size() * ROW_H + PADDING * 2;
        kbPx = windowWidth / 2 - kbPw / 2;
        kbPy = windowHeight / 2 - kbPh / 2;

        renderOpaqueBackground(context);

        RenderUtils.fillRoundRect(context, kbPx, kbPy, kbPw, kbPh, 5, 0xF0141420);
        RenderUtils.fillRoundShadow(context, kbPx, kbPy, kbPw, kbPh, 5, 1, 0xFF00B7FF, 0xFF00B7FF);
        RenderUtils.fillRoundShadow(context, kbPx, kbPy, kbPw, kbPh, 5, 8, 0x4000B7FF, 0x0000B7FF);

        drawText(context, "Keybinds", kbPx + PADDING, kbPy + 4, 0.9F, false);
        RenderUtils.drawDefaultScaledText(context, Component.literal("×"), kbPx + kbPw - CLOSE_W + 2, kbPy + 5, 0.9F, false, 0xFFCC4444);
        drawHorLine(context, kbPx + PADDING, kbPy + TITLE_H - 2, kbPw - PADDING * 2, Shades.GRAY);

        for (int i = 0; i < KEYBIND_ENTRIES.size(); i++) {
            int rowY = kbPy + TITLE_H + PADDING + i * ROW_H;
            Pair<String, String> entry = KEYBIND_ENTRIES.get(i);
            RenderUtils.drawDefaultScaledText(context, Component.literal(entry.left), kbPx + PADDING, rowY, 0.75F, false, 0xFF00B7FF);
            RenderUtils.drawDefaultScaledText(context, Component.literal(entry.right), kbPx + 95, rowY, 0.75F, false, 0xFFCCCCCC);
        }
    }

    public void loadContents() {
        if (loading.isRendering()) return;

        CompletableFuture.runAsync(() -> {
            loading.setRendering(true);
            try {
                if (!FileValidationUtils.validate(currentFile)) throw new IllegalStateException("File not found!");

                StringBuilder sb = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                    reader.lines().forEach(line -> sb.append(line).append("\n"));
                }
                String fileContent = sb.toString();

                mc.execute(() -> {
                    textField.clear();
                    textField.onInput(input -> textField.insertInput(fileContent));
                    textField.clearUndoHistory();
                    textField.shiftEnd();
                });
            } catch (Exception ex) {
                system.printErr("Failed to load IDE contents: " + ex.getMessage());
                mc.execute(() -> textField.clear());
            }
        }).thenRun(() -> loading.setRendering(false));
    }

    public Voidable<CompletableFuture<Void>> saveContents() {
        if (loading.isRendering()) return Voidable.of(null);

        loading.setRendering(true);
        return Voidable.of(CompletableFuture.runAsync(() -> {
            try {
                if (!FileValidationUtils.validate(currentFile)) throw new IllegalStateException("File not found!");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                    writer.write(textField.getContent());
                }
                ReloadCommand.reload();

                if (mc.gui.screen() instanceof ScriptsBrowsingScreen) {
                    mc.gui.setScreen(new ScriptsBrowsingScreen());
                }
            } catch (Exception ex) {
                system.printErr("Error: IDE failed to save script");
                UserInputListener.openPreviousScreen();
            }
        }).whenComplete((r, t) -> loading.setRendering(false)));
    }

    public void deleteScript() {
        try {
            if (currentFile.delete()) ReloadCommand.reload();
            else throw new IllegalStateException("file refused");
        } catch (Exception ex) {
            system.printErr("Error: cannot delete script");
            system.printErr(ex.getMessage());
        }
        BrowsingScreen.currentCategory = Categories.SCRIPTED;
        UserInputListener.openModulesScreen();
    }

    @Override
    public void resize(int width, int height) {
        mc.gui.setScreen(new ClickScriptIDE(currentFile));
    }
}
