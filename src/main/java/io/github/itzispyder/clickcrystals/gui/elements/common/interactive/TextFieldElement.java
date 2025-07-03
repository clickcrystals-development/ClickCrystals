package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.Typeable;
import io.github.itzispyder.clickcrystals.gui.misc.ChatColor;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TextFieldElement extends GuiElement implements Typeable {

    private TextHighlighter highlighter = new TextHighlighter();
    private ChatColor backgroundColor = ChatColor.BLACK;
    private ChatColor textColor = ChatColor.WHITE;
    private int selectionStart, selectionEnd;
    private Point selectedStartPoint, selectedEndPoint;
    private int textY = 5, textHeight;
    private String content = "";
    private String styledContent;
    private boolean selectionBlinking, selectedAll;
    private int selectionBlink;

    public TextFieldElement(String preText, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.selectedStartPoint = new Point();
        this.selectedEndPoint = new Point();
        this.content = preText;
        this.styledContent = style(content);
        this.resetSelection();
    }

    public TextFieldElement(int x, int y, int width, int height) {
        this("", x, y, width, height);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        context.getMatrices().pushMatrix();
        context.enableScissor(x, y, x + width, y + height);

        RenderUtils.fillRect(context, x, y, width, height, backgroundColor.getHex());
        List<OrderedText> text = mc.textRenderer.wrapLines(StringVisitable.plain(styledContent), width - 25);
        textHeight = text.size() * 9;

        int caret = y + textY;
        int color = ChatColor.DARK_GRAY.getHex();

        // lines indexes
        int max = Math.max(99, text.size());
        for (int i = 0; i < max; i++) {
            Text index = Text.literal("" + (i + 1));
            RenderUtils.drawDefaultScaledText(context, index, x + 5, caret + 1, 1.0F, false, color);
            caret += 9;
        }

        // text
        caret = y + textY;
        for (var it = text.iterator(); it.hasNext(); caret += 9) {
            OrderedText line = it.next();
            if (selectedAll) {
                RenderUtils.fillRect(context, x + 20, caret - 1, mc.textRenderer.getWidth(line), 9, 0xA07E75FF);
            }
            context.drawText(mc.textRenderer, line, x + 20, caret, textColor.getHex(), false);
        }

        if (selectionBlinking) {
            int tX = x + 20 + selectedStartPoint.x;
            int tY = y - 1 + textY + selectedStartPoint.y;
            RenderUtils.drawVerLine(context, tX, tY, 9, 0xE0FFFFFF);
        }

        context.disableScissor();
        context.getMatrices().popMatrix();
    }

    @Override
    public void onTick() {
        super.onTick();

        if (mc.currentScreen instanceof GuiScreen screen) {
            if (screen.selected != this) {
                selectionBlinking = false;
                return;
            }

            if (selectionBlink++ >= 20) {
                selectionBlink = 0;
            }
            if (selectionBlink % 10 == 0 && selectionBlink > 0) {
                selectionBlinking = !selectionBlinking;
            }
        }
    }

    @Override
    public void onKey(int key, int scan) {
        if (mc.currentScreen instanceof GuiScreen screen) {
            String typed = GLFW.glfwGetKeyName(key, scan);

            if (key == GLFW.GLFW_KEY_ESCAPE) {
                selectedAll = false;
                screen.selected = null;
            }
            else if (key == GLFW.GLFW_KEY_A && screen.ctrlKeyPressed) {
                selectedAll = true;
            }
            else if (key == GLFW.GLFW_KEY_BACKSPACE) {
                onInput(input -> StringUtils.insertString(content, selectionStart, null));
                shiftLeft();
            }
            else if (key == GLFW.GLFW_KEY_DELETE) {
                onInput(input -> StringUtils.insertString(content, selectionStart + 1, null));
            }
            else if (key == GLFW.GLFW_KEY_SPACE) {
                onInput(input -> insertInput(" "));
                shiftRight();
            }
            else if (key == GLFW.GLFW_KEY_V && screen.ctrlKeyPressed) {
                onInput(input -> insertInput(mc.keyboard.getClipboard()));
                shiftRight();
            }
            else if (key == GLFW.GLFW_KEY_C && screen.ctrlKeyPressed && selectedAll) {
                mc.keyboard.setClipboard(content);
            }
            else if (key == GLFW.GLFW_KEY_ENTER) {
                onInput(input -> insertInput("\n"));
                shiftRight();
                shiftRight();
            }
            else if (key == GLFW.GLFW_KEY_LEFT) {
                shiftLeft();
            }
            else if (key == GLFW.GLFW_KEY_RIGHT) {
                shiftRight();
            }
            else if (key == GLFW.GLFW_KEY_UP) {
                for (int i = 0; i < 10; i++) {
                    shiftLeft();
                }
            }
            else if (key == GLFW.GLFW_KEY_DOWN) {
                for (int i = 0; i < 10; i++) {
                    shiftRight();
                }
            }
            else if (typed != null){
                onInput(input -> insertInput(screen.shiftKeyPressed ? StringUtils.keyPressWithShift(typed) : typed));
                shiftRight();
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.selectedAll = false;
    }

    @Override
    public void onInput(Function<String, String> factory) {
        if (selectedAll) {
            content = styledContent = "";
            selectedAll = false;
            resetSelection();
        }
        content = factory.apply(content);
        updateSelection();
        this.styledContent = style(content);
    }

    public void shiftRight() {
        selectionStart = MathUtils.clamp(selectionStart + 1, 0, content.length());
        selectionEnd = selectionStart;
        updateSelection();
    }

    public void shiftLeft() {
        selectionStart = MathUtils.clamp(selectionStart - 1, 0, content.length());
        selectionEnd = selectionStart;
        updateSelection();
    }

    public void shiftStart() {
        selectionStart = MathUtils.clamp(0, 0, content.length());
        selectionEnd = selectionStart;
        textY = 5;
        updateSelection();
    }

    public void shiftEnd() {
        selectionStart = MathUtils.clamp(content.length(), 0, content.length());
        selectionEnd = selectionStart;
        textY = 5 - textHeight;
        updateSelection();
    }

    public String insertInput(String input) {
        return StringUtils.insertString(content, selectionStart, input);
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, int amount) {
        super.mouseScrolled(mouseX, mouseY, amount);

        for (int i = 0; i < ScrollPanelElement.SCROLL_MULTIPLIER; i++) {
            textY = MathUtils.clamp(textY + amount, 5 - textHeight, 5);
        }
    }

    public String style(String s) {
        if (s == null || s.isEmpty()) {
            return " ";
        }
        this.updateSelection();
        return highlighter.highlightText(s);
    }

    public void resetSelection() {
        selectionStart = selectionEnd = 0;
        updateSelection();
    }

    public void updateSelection() {
        String str = content.substring(0, MathUtils.clamp(selectionStart, 0, content.length()));
        List<OrderedText> lines = mc.textRenderer.wrapLines(StringVisitable.plain(str), width - 25);

        if (lines == null || lines.isEmpty()) {
            selectedStartPoint.setLocation(0, 0);
            return;
        }
        selectedStartPoint.x = mc.textRenderer.getWidth(lines.get(Math.max(0, lines.size() - 1)));
        selectedStartPoint.y = lines.size() * 9 - 9;
    }

    public String[] getLines() {
        return content.lines().toArray(String[]::new);
    }

    public String getContent() {
        return content;
    }

    public ChatColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(ChatColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ChatColor getTextColor() {
        return textColor;
    }

    public void setTextColor(ChatColor textColor) {
        this.textColor = textColor;
    }

    public TextHighlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(TextHighlighter highlighter) {
        this.highlighter = highlighter;
    }

    public void clear() {
        content = styledContent = "";
        resetSelection();
    }



    // highlighters
    public static class TextHighlighter {
        private List<HighlightFactory> stringFactories = new ArrayList<>();
        private ChatColor originalColor;

        public TextHighlighter(ChatColor originalColor) {
            this.originalColor = originalColor;
        }

        public TextHighlighter() {
            this(ChatColor.WHITE);
        }

        public String highlightText(String text) {
            String[] lines = text.lines().toArray(String[]::new);
            String result = "";

            for (int i = 0; i < lines.length; i++) { // for each line
                String line = lines[i];
                String[] words = line.split(" ");

                for (int j = 0; j < words.length; j++) { // for each word
                    String word = words[j];

                    if (word.isEmpty()) { // skip empty word (means more than one spaces in a row)
                        result = result.concat(" ");
                        continue;
                    }

                    String r = word;
                    for (HighlightFactory factory : stringFactories) { // apply style
                        var product = factory.process(r);
                        if (product.right) {
                            r = product.left;
                            break;
                        }
                    }

                    result = result.concat(r + (j < words.length - 1? " " : "")); // add to result
                }

                if (i < lines.length - 1) {
                    result = result.concat("\n"); // add new line to result
                }
            }
            return result;
        }

        private HighlightFactory colorStringFactory(ChatColor color, String str) {
            return new HighlightFactory(s -> s.replace("\n", "").equals(str), s -> "%s%s%s".formatted(color, s, originalColor));
        }

        private HighlightFactory predicateStringFactory(ChatColor color, Predicate<String> predicate) {
            return new HighlightFactory(predicate, s -> "%s%s%s".formatted(color, s, originalColor));
        }

        public TextHighlighter put(ChatColor color, String... keys) {
            for (String key : keys) {
                if (key != null && !key.isEmpty()) {
                    stringFactories.add(colorStringFactory(color, key));
                }
            }
            return this;
        }

        public TextHighlighter put(ChatColor color, Iterable<String> keys) {
            for (String key : keys) {
                if (key != null && !key.isEmpty()) {
                    stringFactories.add(colorStringFactory(color, key));
                }
            }
            return this;
        }

        public TextHighlighter put(ChatColor color, Predicate<String> keys) {
            stringFactories.add(predicateStringFactory(color, keys));
            return this;
        }

        public TextHighlighter put(Predicate<String> keys, Function<String, String> factory) {
            stringFactories.add(new HighlightFactory(keys, factory));
            return this;
        }

        public TextHighlighter setStringFactory(List<HighlightFactory> stringFactories) {
            this.stringFactories = stringFactories;
            return this;
        }

        public void clearFactories() {
            stringFactories.clear();
        }

        public ChatColor getOriginalColor() {
            return originalColor;
        }

        public void setOriginalColor(ChatColor originalColor) {
            this.originalColor = originalColor;
        }

        public record HighlightFactory(Predicate<String> predicate, Function<String, String> factory) {
            public Pair<String, Boolean> process(String str) {
                if (predicate.test(str)) {
                    return Pair.of(factory.apply(str), true);
                }
                return Pair.of(str, false);
            }
        }
    }
}
