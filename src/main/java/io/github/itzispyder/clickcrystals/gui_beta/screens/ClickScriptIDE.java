package io.github.itzispyder.clickcrystals.gui_beta.screens;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui_beta.elements.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.interactive.TextFieldElement;
import io.github.itzispyder.clickcrystals.gui_beta.misc.ChatColor;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Gray;
import io.github.itzispyder.clickcrystals.gui_beta.misc.Tex;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.modules.scripts.*;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ClickScriptIDE extends DefaultBase {

    public static final TextFieldElement.TextHighlighter CLICKSCRIPT_HIGHLIGHTER = new TextFieldElement.TextHighlighter() {{
        ChatColor og = getOriginalColor();
        Function<ChatColor, Function<String, String>> applyColor = c -> s -> "%s%s%s".formatted(c, s, og);

        this.put(ChatColor.ORANGE, ClickScript.collectNames());
        this.put(ChatColor.GRAY, "then");
        this.put(s -> StringUtils.startsWithAny(s, ":", "#"), applyColor.apply(ChatColor.DARK_GREEN));
        this.put(s -> s.replaceAll("[0-9><=!.+-]", "").isEmpty(), applyColor.apply(ChatColor.DARK_AQUA));
        this.put(ChatColor.YELLOW, Arrays.stream(InputCmd.Action.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(OnEventCmd.EventType.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(ModuleCmd.Action.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(TurnToCmd.Mode.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(IfCmd.ConditionType.values()).map(e -> e.name().toLowerCase()).toList());
    }};
    private final ScriptedModule module;
    private final LoadingIconElement loading;

    public TextFieldElement textField = new TextFieldElement(contentX, contentY + 21, contentWidth, contentHeight - 21) {{
        this.setHighlighter(CLICKSCRIPT_HIGHLIGHTER);
    }};

    public ClickScriptIDE(ScriptedModule module) {
        super("ClickScript IDE");
        this.addChild(textField);
        this.module = module;

        this.loading = new LoadingIconElement(contentX + contentWidth / 2 - 10, contentY + contentHeight / 2 - 10, 20);
        this.loading.setRendering(false);
        this.addChild(loading);

        this.loadContents();
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderDefaultBase(context);

        // content
        int caret = contentY + 10;
        RenderUtils.drawTexture(context, Tex.ICON_CLICKSCRIPT, contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, "Editing '%s'".formatted(module.filename), contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
    }

    public void loadContents() {
        if (loading.isRendering()) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            loading.setRendering(true);
            try {
                File file = new File(module.filepath);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String str = "";

                for (var i = reader.lines().iterator(); i.hasNext();) {
                    str = str.concat(i.next() + " \n");
                }

                String finalStr = str;
                textField.clear();
                textField.onInput(input -> textField.insertInput(finalStr));
                textField.shiftEnd();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                UserInputListener.openPreviousScreen();
            }
        }).thenRun(() -> {
            loading.setRendering(false);
        });
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        mc.setScreen(new ClickScriptIDE(module));
    }
}
