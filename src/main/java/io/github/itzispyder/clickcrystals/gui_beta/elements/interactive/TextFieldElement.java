package io.github.itzispyder.clickcrystals.gui_beta.elements.interactive;

import io.github.itzispyder.clickcrystals.gui_beta.GuiElement;
import io.github.itzispyder.clickcrystals.gui_beta.elements.Typeable;
import io.github.itzispyder.clickcrystals.gui_beta.misc.ChatColor;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class TextFieldElement extends GuiElement implements Typeable {

    private TextHighlighter highlighter = new TextHighlighter();
    private ChatColor backgroundColor = ChatColor.BLACK;
    private ChatColor textColor = ChatColor.WHITE;
    private int selectionStart, selectionEnd;
    private int textY = 5, textHeight;
    private String content = "";
    private String styledContent;

    public TextFieldElement(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.resetSelection();
        this.styledContent = style(content);
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY) {
        context.getMatrices().push();
        context.enableScissor(x, y, x + width, y + height);

        RenderUtils.fill(context, x, y, width, height, backgroundColor.getHex());
        List<OrderedText> text = mc.textRenderer.wrapLines(StringVisitable.plain(styledContent), width - 25);
        textHeight = text.size() * 9;

        int caret = y + textY;
        int color = ChatColor.DARK_GRAY.getHex();
        int i = 0;

        for (var it = text.iterator(); it.hasNext(); caret += 9) {
            OrderedText line = it.next();
            Text index = Text.literal("" + (i++ + 1));
            RenderUtils.drawDefaultScaledText(context, index, x + 5, caret + 1, 1.0F, false, color);
            context.drawText(mc.textRenderer, line, x + 20, caret, textColor.getHex(), false);
        }

        context.disableScissor();
        context.getMatrices().pop();
    }

    @Override
    public void onKey(int key, int scan) {
        Typeable.super.onKey(key, scan);

        if (key == GLFW.GLFW_KEY_ENTER) {
            onInput(input -> input.concat(" \n"));
        }

        this.styledContent = style(content);
    }

    @Override
    public void onInput(Function<String, String> factory) {
        content = factory.apply(content);
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, int amount) {
        super.mouseScrolled(mouseX, mouseY, amount);

        for (int i = 0; i < ScrollPanelElement.SCROLL_MULTIPLIER; i++) {
            textY = MathUtils.minMax(textY + amount, 5 - textHeight, 5);
        }
    }

    public String style(String s) {
        if (s == null || s.isEmpty()) {
            return " ";
        }
        return highlighter.highlightText(s);
    }

    public void resetSelection() {
        selectionStart = selectionEnd = 0;
    }

    public String[] getLines() {
        return content.split("\n");
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



    // highlighters
    public static class TextHighlighter {
        private Set<HighlightFactory> stringFactories = new HashSet<>();
        private ChatColor originalColor;

        public TextHighlighter(ChatColor originalColor) {
            this.originalColor = originalColor;
        }

        public TextHighlighter() {
            this(ChatColor.WHITE);
        }

        public String highlightText(String text) {
            for (String k : text.split(" ")) {
                k = k.replaceAll("\n", "");
                if (k.isEmpty()) {
                    continue;
                }

                String r = k;
                for (HighlightFactory factory : stringFactories) {
                    r = factory.process(r);
                }
                text = text.replaceAll(k, r);
            }
            return text;
        }

        private HighlightFactory colorStringFactory(ChatColor color, String str) {
            return new HighlightFactory(s -> s.replaceAll("\n", "").equals(str), s -> "%s%s%s".formatted(color, s, originalColor));
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

        public TextHighlighter setStringFactory(Set<HighlightFactory> stringFactories) {
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
            public String process(String str) {
                if (predicate.test(str)) {
                    return factory.apply(str);
                }
                return str;
            }
        }
    }
}
