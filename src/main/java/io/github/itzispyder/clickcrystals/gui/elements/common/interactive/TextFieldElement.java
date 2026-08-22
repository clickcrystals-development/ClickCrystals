package io.github.itzispyder.clickcrystals.gui.elements.common.interactive;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.Typeable;
import io.github.itzispyder.clickcrystals.gui.misc.ChatColor;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import com.mojang.blaze3d.platform.InputConstants;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TextFieldElement extends GuiElement implements Typeable {

    private static final int UNDO_LIMIT = 100;
    private static final int GUTTER_COLOR = 0xFF4A5568;
    private static final int SEL_COLOR = 0xA07E75FF;
    private static final int CURSOR_COLOR = 0xE0FFFFFF;

    private TextHighlighter highlighter = new TextHighlighter();
    private ChatColor backgroundColor = ChatColor.BLACK;
    private ChatColor textColor = ChatColor.WHITE;
    private int selectionStart, selectionEnd;
    private final Point selectedStartPoint;
    private int textY = 5, textHeight;
    private String content = "";
    private String styledContent;
    private boolean selectionBlinking, selectedAll;
    private int selectionBlink;
    private final ArrayDeque<Pair<String, Integer>> undoStack = new ArrayDeque<>();
    private final ArrayDeque<Pair<String, Integer>> redoStack = new ArrayDeque<>();
    private int preferredCol = -1;
    private int dragAnchor = -1;
    private Function<Integer, Boolean> keyInterceptor;
    private Consumer<Boolean> onStateChanged; // fires on any cursor/content change; arg = caused by typing

    private List<FormattedCharSequence> cachedRows = List.of();

    public TextFieldElement(String preText, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.selectedStartPoint = new Point();
        this.content = preText;
        this.styledContent = style(content);
        this.resetSelection();
    }

    public TextFieldElement(int x, int y, int width, int height) {
        this("", x, y, width, height);
    }

    private int lineStart(int pos) {
        int i = Math.min(pos, content.length());
        while (i > 0 && content.charAt(i - 1) != '\n') i--;
        return i;
    }

    private int lineEnd(int pos) {
        int i = Math.min(pos, content.length());
        while (i < content.length() && content.charAt(i) != '\n') i++;
        return i;
    }

    private int prevWordBoundary(int pos) {
        int i = pos;
        while (i > 0 && Character.isWhitespace(content.charAt(i - 1))) i--;
        while (i > 0 && !Character.isWhitespace(content.charAt(i - 1))) i--;
        return i;
    }

    private int nextWordBoundary(int pos) {
        int i = pos;
        while (i < content.length() && !Character.isWhitespace(content.charAt(i))) i++;
        while (i < content.length() && Character.isWhitespace(content.charAt(i))) i++;
        return i;
    }

    private boolean hasRange() {
        return !selectedAll && selectionStart != selectionEnd;
    }

    private int selMin() {
        return Math.min(selectionStart, selectionEnd);
    }

    private int selMax() {
        return Math.max(selectionStart, selectionEnd);
    }

    // Comments or uncomments every line touched by the cursor or selection (Ctrl+A then Ctrl+/ covers all lines).
    private boolean toggleComment() {
        int from = selectedAll ? 0 : selMin();
        int to = selectedAll ? content.length() : selMax();
        int blockStart = lineStart(from);
        int blockEnd = lineEnd(to);
        boolean collapsed = !selectedAll && !hasRange();

        String block = content.substring(blockStart, blockEnd);
        String[] lines = block.split("\n", -1);

        // Uncomment only when every non-blank line is already commented; otherwise comment them all.
        boolean allCommented = true;
        for (String line : lines)
            if (!line.isBlank() && !line.startsWith("//")) {
                allCommented = false;
                break;
            }

        StringBuilder rebuilt = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.isBlank())
                line = allCommented ? line.substring(line.startsWith("// ") ? 3 : 2) : "// " + line;
            rebuilt.append(line);
            if (i < lines.length - 1) rebuilt.append("\n");
        }

        pushUndo();
        int savedStart = selectionStart;
        content = content.substring(0, blockStart) + rebuilt + content.substring(blockEnd);

        if (collapsed) {
            int delta = rebuilt.length() - block.length();
            selectionStart = selectionEnd = MathUtils.clamp(savedStart + delta, blockStart, blockStart + rebuilt.length());
        } else {
            selectionStart = blockStart;
            selectionEnd = blockStart + rebuilt.length();
        }
        styledContent = style(content);
        updateSelection();
        preferredCol = -1;
        return true;
    }

    private void deleteRange() {
        pushUndo();
        int lo = selectedAll ? 0              : selMin();
        int hi = selectedAll ? content.length() : selMax();
        content = content.substring(0, lo) + content.substring(hi);
        selectionStart = selectionEnd = lo;
        selectedAll = false;
        styledContent = style(content);
        updateSelection();
    }

    private void pushUndo() {
        if (!undoStack.isEmpty() && undoStack.peek().left.equals(content)) return;
        undoStack.push(Pair.of(content, selectionStart));
        redoStack.clear();
        while (undoStack.size() > UNDO_LIMIT) undoStack.removeLast();
    }

    @Override
    public void onChar(char chr) {
        if (Character.isISOControl(chr)) return;

        char closing = matchingClose(chr);

        // Wrap selection in brackets rather than replacing it.
        if (closing != 0 && (hasRange() || selectedAll)) {
            wrapSelection(chr, closing);
            preferredCol = -1;
            fireChanged(true);
            return;
        }

        if (hasRange() || selectedAll) deleteRange();

        // Skip over a closing bracket that was auto-inserted.
        if (isClosingBracket(chr) && selectionStart < content.length() && content.charAt(selectionStart) == chr) {
            shiftRight();
            preferredCol = -1;
            fireChanged(true);
            return;
        }

        // Auto-close: ( -> (), [ -> [], { -> {}
        if (closing != 0) {
            final char c = closing;
            onInput(input -> insertInput(chr + String.valueOf(c)));
            shiftRight();
            preferredCol = -1;
            fireChanged(true);
            return;
        }

        onInput(input -> insertInput(String.valueOf(chr)));
        shiftRight();
        preferredCol = -1;
        fireChanged(true);
    }

    // Returns the matching closing bracket for an opening bracket, or 0 if not a bracket.
    private static char matchingClose(char open) {
        return switch (open) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            default -> 0;
        };
    }

    private static boolean isClosingBracket(char c) {
        return c == ')' || c == ']' || c == '}';
    }

    private void wrapSelection(char open, char close) {
        int lo = selectedAll ? 0 : selMin();
        int hi = selectedAll ? content.length() : selMax();
        pushUndo();
        content = content.substring(0, lo) + open + content.substring(lo, hi) + close + content.substring(hi);
        selectionStart = lo + 1;
        selectionEnd = hi + 1;
        selectedAll = false;
        styledContent = style(content);
        updateSelection();
    }

    @Override
    public boolean onKey(int key, int scan) {
        if (!(mc.gui.screen() instanceof GuiScreen screen)) return false;
        if (keyInterceptor != null && keyInterceptor.apply(key)) return true;
        boolean handled = handleKeyInner(key, screen);
        // Only typing (character entry, backspace, delete) should surface autocomplete; copy/paste/navigation dismiss it.
        if (handled) fireChanged(key == InputConstants.KEY_BACKSPACE || key == InputConstants.KEY_DELETE);
        return handled;
    }

    private boolean handleKeyInner(int key, GuiScreen screen) {
        if (key == InputConstants.KEY_ESCAPE) {
            if (hasRange() || selectedAll) {
                selectedAll = false;
                selectionEnd = selectionStart;
                return true;
            }
            screen.selected = null;
            return true;
        }

        // Ctrl combos
        if (screen.ctrlKeyPressed) {
            switch (key) {
                case InputConstants.KEY_A -> {
                    selectedAll = true;
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_C -> {
                    if (selectedAll) mc.keyboardHandler.setClipboard(content);
                    else if (hasRange()) mc.keyboardHandler.setClipboard(content.substring(selMin(), selMax()));
                    return true;
                }
                case InputConstants.KEY_X -> {
                    if (selectedAll) {
                        mc.keyboardHandler.setClipboard(content);
                        clear();
                    } else if (hasRange()) {
                        mc.keyboardHandler.setClipboard(content.substring(selMin(), selMax()));
                        deleteRange();
                    }
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_V -> {
                    if (hasRange() || selectedAll) deleteRange();
                    onInput(input -> insertInput(mc.keyboardHandler.getClipboard()));
                    shiftRight();
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_Z -> {
                    if (!undoStack.isEmpty()) {
                        redoStack.push(Pair.of(content, selectionStart));
                        Pair<String, Integer> state = undoStack.pop();
                        content = state.left;
                        selectionStart = selectionEnd = MathUtils.clamp(state.right, 0, content.length());
                        styledContent = style(content);
                        updateSelection();
                    }
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_Y -> {
                    if (!redoStack.isEmpty()) {
                        undoStack.push(Pair.of(content, selectionStart));
                        Pair<String, Integer> state = redoStack.pop();
                        content = state.left;
                        selectionStart = selectionEnd = MathUtils.clamp(state.right, 0, content.length());
                        styledContent = style(content);
                        updateSelection();
                    }
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_D -> {
                    int ls = lineStart(selectionStart);
                    int le = lineEnd(selectionStart);
                    String line = content.substring(ls, le);
                    pushUndo();
                    String ins = "\n" + line;
                    content = content.substring(0, le) + ins + content.substring(le);
                    selectionStart = selectionEnd = le + ins.length();
                    styledContent = style(content);
                    updateSelection();
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_SLASH -> {
                    return toggleComment();
                }
                case InputConstants.KEY_LEFT -> {
                    selectionStart = selectionEnd = prevWordBoundary(selectionStart);
                    selectedAll = false;
                    updateSelection();
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_RIGHT -> {
                    selectionStart = selectionEnd = nextWordBoundary(selectionStart);
                    selectedAll = false;
                    updateSelection();
                    preferredCol = -1;
                    return true;
                }
                case InputConstants.KEY_BACKSPACE -> {
                    if (hasRange() || selectedAll) {
                        deleteRange();
                        preferredCol = -1;
                        return true;
                    }
                    int wordStart = prevWordBoundary(selectionStart);
                    if (wordStart < selectionStart) {
                        pushUndo();
                        content = content.substring(0, wordStart) + content.substring(selectionStart);
                        selectionStart = selectionEnd = wordStart;
                        styledContent = style(content);
                        updateSelection();
                    }
                    preferredCol = -1;
                    return true;
                }
            }
        }

        // Basic keys
        switch (key) {
            case InputConstants.KEY_BACKSPACE -> {
                if (hasRange() || selectedAll) {
                    deleteRange();
                    preferredCol = -1;
                    return true;
                }
                // Smart delete: if cursor sits between an empty matching pair, delete both.
                if (selectionStart > 0 && selectionStart < content.length()) {
                    char expectedClose = matchingClose(content.charAt(selectionStart - 1));
                    if (expectedClose != 0 && content.charAt(selectionStart) == expectedClose) {
                        pushUndo();
                        content = content.substring(0, selectionStart - 1) + content.substring(selectionStart + 1);
                        selectionStart = selectionEnd = selectionStart - 1;
                        styledContent = style(content);
                        updateSelection();
                        preferredCol = -1;
                        return true;
                    }
                }
                onInput(cur -> selectionStart > 0 && !cur.isEmpty()
                        ? cur.substring(0, selectionStart - 1) + cur.substring(selectionStart)
                        : cur);
                shiftLeft();
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_DELETE -> {
                if (hasRange() || selectedAll) {
                    deleteRange();
                    preferredCol = -1;
                    return true;
                }
                onInput(input -> StringUtils.insertString(content, selectionStart + 1, null));
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_RETURN -> {
                if (hasRange() || selectedAll) deleteRange();
                int ls = lineStart(selectionStart);
                String before = content.substring(ls, selectionStart);
                int spaces = 0;
                while (spaces < before.length() && before.charAt(spaces) == ' ') spaces++;

                boolean betweenBraces = selectionStart > 0
                        && selectionStart < content.length()
                        && content.charAt(selectionStart - 1) == '{'
                        && content.charAt(selectionStart) == '}';

                pushUndo();
                if (betweenBraces) {
                    // Place cursor on indented inner line, closing brace on its own line below.
                    String inner = "\n" + " ".repeat(spaces + 4);
                    String outer = "\n" + " ".repeat(spaces);
                    content = content.substring(0, selectionStart) + inner + outer + content.substring(selectionStart);
                    selectionStart = selectionEnd = selectionStart + inner.length();
                } else {
                    String indent = "\n" + " ".repeat(spaces);
                    content = content.substring(0, selectionStart) + indent + content.substring(selectionStart);
                    selectionStart = selectionEnd = selectionStart + indent.length();
                }
                styledContent = style(content);
                updateSelection();
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_TAB -> {
                if (hasRange() || selectedAll) deleteRange();
                onInput(input -> insertInput("    "));
                for (int i = 0; i < 4; i++) shiftRight();
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_LEFT -> {
                if (hasRange() || selectedAll) {
                    selectionStart = selectionEnd = selMin();
                    selectedAll = false;
                    updateSelection();
                } else shiftLeft();
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_RIGHT -> {
                if (hasRange() || selectedAll) {
                    selectionStart = selectionEnd = selMax();
                    selectedAll = false;
                    updateSelection();
                } else shiftRight();
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_HOME -> {
                selectionStart = selectionEnd = lineStart(selectionStart);
                selectedAll = false;
                updateSelection();
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_END -> {
                selectionStart = selectionEnd = lineEnd(selectionStart);
                selectedAll = false;
                updateSelection();
                preferredCol = -1;
                return true;
            }
            case InputConstants.KEY_UP -> {
                navigateVertical(-1);
                return true;
            }
            case InputConstants.KEY_DOWN -> {
                navigateVertical(1);
                return true;
            }
        }
        return false;
    }

    private void navigateVertical(int dir) {
        selectedAll = false;
        int col = preferredCol >= 0 ? preferredCol : selectionStart - lineStart(selectionStart);
        preferredCol = col;

        String[] lines = content.split("\n", -1);
        int charPos = 0, curLine = 0;
        for (int i = 0; i < lines.length; i++) {
            if (charPos + lines[i].length() >= selectionStart) {
                curLine = i;
                break;
            }
            charPos += lines[i].length() + 1;
        }

        int targetLine = MathUtils.clamp(curLine + dir, 0, lines.length - 1);
        int targetPos = 0;
        for (int i = 0; i < targetLine; i++) targetPos += lines[i].length() + 1;
        targetPos += Math.min(col, lines[targetLine].length());
        selectionStart = selectionEnd = targetPos;
        updateSelection();
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        selectedAll = false;
        if (!isHovered((int) mouseX, (int) mouseY)) return;

        if (button == 0) {
            int pos = pixelToContentPos(mouseX, mouseY);
            selectionStart = selectionEnd = pos;
            dragAnchor = pos;
            preferredCol = -1;
            updateSelection();
            fireChanged(false); // repositioning the caret with the mouse dismisses autocomplete
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (button == 0) dragAnchor = -1;
    }

    public void onDrag(double mouseX, double mouseY) {
        if (dragAnchor < 0 || !isHovered((int) mouseX, (int) mouseY)) return;
        int pos = pixelToContentPos(mouseX, mouseY);
        selectionEnd = dragAnchor;
        selectionStart = pos;
        selectedAll = false;
        updateSelection();
    }

    private int pixelToContentPos(double mouseX, double mouseY) {
        List<FormattedCharSequence> rows = cachedRows.isEmpty()
                ? mc.font.split(FormattedText.of(styledContent), width - 25)
                : cachedRows;
        int targetRow = MathUtils.clamp((int) ((mouseY - (y + textY)) / 9), 0, Math.max(0, rows.size() - 1));


        int pos = 0, row = 0, rowStart = 0;
        while (pos <= content.length() && row < targetRow) {
            int next = pos + 1;
            List<FormattedCharSequence> r = mc.font.split(FormattedText.of(content.substring(0, next)), width - 25);
            if (r.size() > row + 1) {
                row++;
                rowStart = next;
            }
            pos = next;
        }

        int best = rowStart;
        double bestDist = Double.MAX_VALUE;
        double targetX = mouseX - (x + 20);

        for (int p = rowStart; p <= content.length(); p++) {
            List<FormattedCharSequence> r = mc.font.split(FormattedText.of(content.substring(0, p)), width - 25);
            if (r.size() > targetRow + 1) break;
            double cx = r.isEmpty() ? 0 : mc.font.width(r.get(Math.min(targetRow, r.size() - 1)));
            double dist = Math.abs(cx - targetX);
            if (dist <= bestDist) {
                bestDist = dist;
                best = p;
            }
        }

        return MathUtils.clamp(best, 0, content.length());
    }


    @Override
    public void onRender(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        context.pose().pushMatrix();
        context.enableScissor(x, y, x + width, y + height);

        RenderUtils.fillRect(context, x, y, width, height, backgroundColor.getHex());
        List<FormattedCharSequence> text = mc.font.split(FormattedText.of(styledContent), width - 25);
        cachedRows = text;
        textHeight = text.size() * 9;

        int caret = y + textY;
        int lineCount = Math.max(1, text.size());
        for (int i = 0; i < lineCount; i++) {
            RenderUtils.drawDefaultScaledText(context, Component.literal(String.valueOf(i + 1)), x + 5, caret + 1, 1.0F, false, GUTTER_COLOR);
            caret += 9;
        }

        if (hasRange()) renderRangeHighlight(context, text);

        if (selectedAll) {
            caret = y + textY;
            for (FormattedCharSequence line : text) {
                RenderUtils.fillRect(context, x + 20, caret - 1, mc.font.width(line), 9, SEL_COLOR);
                caret += 9;
            }
        }

        caret = y + textY;
        for (FormattedCharSequence line : text) {
            context.text(mc.font, line, x + 20, caret, textColor.getHex(), false);
            caret += 9;
        }

        if (selectionBlinking) {
            RenderUtils.drawVerLine(context, x + 20 + selectedStartPoint.x, y - 1 + textY + selectedStartPoint.y, 9, CURSOR_COLOR);
        }

        context.disableScissor();
        context.pose().popMatrix();
    }

    private void renderRangeHighlight(GuiGraphicsExtractor context, List<FormattedCharSequence> rows) {
        Point startPt = computePointFor(selMin());
        Point endPt = computePointFor(selMax());
        int startRow = startPt.y / 9;
        int endRow = endPt.y / 9;

        for (int row = startRow; row <= endRow; row++) {
            int rx0 = (row == startRow) ? startPt.x : 0;
            int rx1 = (row == endRow) ? endPt.x : (row < rows.size() ? mc.font.width(rows.get(row)) : 0);
            int ry = y + textY - 1 + row * 9;
            RenderUtils.fillRect(context, x + 20 + rx0, ry, Math.max(1, rx1 - rx0), 9, SEL_COLOR);
        }
    }

    private Point computePointFor(int pos) {
        String sub = content.substring(0, MathUtils.clamp(pos, 0, content.length()));
        List<FormattedCharSequence> lines = mc.font.split(FormattedText.of(sub), width - 25);
        if (lines == null || lines.isEmpty()) return new Point(0, 0);
        return new Point(mc.font.width(lines.get(lines.size() - 1)), (lines.size() - 1) * 9);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (mc.gui.screen() instanceof GuiScreen screen) {
            if (screen.selected != this) {
                selectionBlinking = false;
                return;
            }
            if (selectionBlink++ >= 20) selectionBlink = 0;
            if (selectionBlink % 10 == 0 && selectionBlink > 0) selectionBlinking = !selectionBlinking;
        }
    }

    @Override
    public void onInput(Function<String, String> factory) {
        if (selectedAll) {
            content = styledContent = "";
            selectedAll = false;
            resetSelection();
        }
        pushUndo();
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
        selectionStart = selectionEnd = 0;
        textY = 5;
        updateSelection();
    }

    public void shiftEnd() {
        selectionStart = selectionEnd = content.length();
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
        if (s == null || s.isEmpty()) return " ";
        return highlighter.highlightText(s);
    }

    public void resetSelection() {
        selectionStart = selectionEnd = 0;
        updateSelection();
    }

    public void updateSelection() {
        String str = content.substring(0, MathUtils.clamp(selectionStart, 0, content.length()));
        List<FormattedCharSequence> lines = mc.font.split(FormattedText.of(str), width - 25);
        if (lines == null || lines.isEmpty()) {
            selectedStartPoint.setLocation(0, 0);
            return;
        }
        selectedStartPoint.x = mc.font.width(lines.get(Math.max(0, lines.size() - 1)));
        selectedStartPoint.y = lines.size() * 9 - 9;
    }

    public String getWordBeforeCursor() {
        int start = selectionStart;
        while (start > 0 && content.charAt(start - 1) != '\n' && !Character.isWhitespace(content.charAt(start - 1)))
            start--;
        return content.substring(start, selectionStart);
    }

    public String getCurrentLine() {
        return content.substring(lineStart(selectionStart), lineEnd(selectionStart));
    }

    public int getCursorColInLine() {
        return selectionStart - lineStart(selectionStart);
    }

    public int getCursorPixelX() {
        return x + 20 + selectedStartPoint.x;
    }

    public int getCursorPixelY() {
        return y + textY + selectedStartPoint.y;
    }

    public void insertCompletion(String completion) {
        int wordStart = selectionStart;
        while (wordStart > 0 && content.charAt(wordStart - 1) != '\n' && !Character.isWhitespace(content.charAt(wordStart - 1)))
            wordStart--;
        pushUndo();
        content = content.substring(0, wordStart) + completion + content.substring(selectionStart);
        selectionStart = selectionEnd = wordStart + completion.length();
        styledContent = style(content);
        updateSelection();
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

    public void setBackgroundColor(ChatColor c) {
        this.backgroundColor = c;
    }

    public ChatColor getTextColor() {
        return textColor;
    }

    public void setTextColor(ChatColor c) {
        this.textColor = c;
    }

    public TextHighlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(TextHighlighter h) {
        this.highlighter = h;
    }

    public void setKeyInterceptor(Function<Integer, Boolean> interceptor) {
        this.keyInterceptor = interceptor;
    }

    public void setOnContentChanged(Consumer<Boolean> callback) {
        this.onStateChanged = callback;
    }

    // typed = true when the change came from the user entering/removing text (not copy, paste, navigation or mouse).
    private void fireChanged(boolean typed) {
        if (onStateChanged != null) onStateChanged.accept(typed);
    }

    public void clear() {
        content = styledContent = "";
        undoStack.clear();
        redoStack.clear();
        dragAnchor = -1;
        preferredCol = -1;
        cachedRows = List.of();
        resetSelection();
    }

    public void clearUndoHistory() {
        undoStack.clear();
        redoStack.clear();
    }

    public static class TextHighlighter {
        private List<HighlightFactory> stringFactories = new ArrayList<>();
        private ChatColor originalColor;
        private String commentPrefix;
        private ChatColor commentColor = ChatColor.GRAY;

        public TextHighlighter(ChatColor originalColor) {
            this.originalColor = originalColor;
        }

        public TextHighlighter() {
            this(ChatColor.WHITE);
        }

        // Renders whole lines starting with the given prefix in a single comment color.
        public TextHighlighter comments(String prefix, ChatColor color) {
            this.commentPrefix = prefix;
            this.commentColor = color;
            return this;
        }

        public String highlightText(String text) {
            String[] lines = text.lines().toArray(String[]::new);
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                if (commentPrefix != null && lines[i].stripLeading().startsWith(commentPrefix)) {
                    result.append("%s%s%s".formatted(commentColor, lines[i], originalColor));
                    if (i < lines.length - 1) result.append("\n");
                    continue;
                }
                String[] words = lines[i].split(" ");
                for (int j = 0; j < words.length; j++) {
                    String word = words[j];
                    if (word.isEmpty()) {
                        result.append(" ");
                        continue;
                    }
                    String r = word;
                    for (HighlightFactory factory : stringFactories) {
                        Pair<String, Boolean> product = factory.process(r);
                        if (product.right) {
                            r = product.left;
                            break;
                        }
                    }
                    result.append(r).append(j < words.length - 1 ? " " : "");
                }
                if (i < lines.length - 1) result.append("\n");
            }
            return result.toString();
        }

        private HighlightFactory colorStringFactory(ChatColor color, String str) {
            return new HighlightFactory(s -> s.replace("\n", "").equals(str), s -> "%s%s%s".formatted(color, s, originalColor));
        }

        private HighlightFactory predicateStringFactory(ChatColor color, Predicate<String> predicate) {
            return new HighlightFactory(predicate, s -> "%s%s%s".formatted(color, s, originalColor));
        }

        public TextHighlighter put(ChatColor color, String... keys) {
            for (String key : keys)
                if (key != null && !key.isEmpty()) stringFactories.add(colorStringFactory(color, key));
            return this;
        }

        public TextHighlighter put(ChatColor color, Iterable<String> keys) {
            for (String key : keys)
                if (key != null && !key.isEmpty()) stringFactories.add(colorStringFactory(color, key));
            return this;
        }

        public TextHighlighter put(ChatColor color, Predicate<String> predicate) {
            stringFactories.add(predicateStringFactory(color, predicate));
            return this;
        }

        public TextHighlighter put(Predicate<String> predicate, Function<String, String> factory) {
            stringFactories.add(new HighlightFactory(predicate, factory));
            return this;
        }

        public TextHighlighter setStringFactory(List<HighlightFactory> factories) {
            this.stringFactories = factories;
            return this;
        }

        public void clearFactories() {
            stringFactories.clear();
        }

        public ChatColor getOriginalColor() {
            return originalColor;
        }

        public void setOriginalColor(ChatColor c) {
            this.originalColor = c;
        }

        public record HighlightFactory(Predicate<String> predicate, Function<String, String> factory) {
            public Pair<String, Boolean> process(String str) {
                if (predicate.test(str)) return Pair.of(factory.apply(str), true);
                return Pair.of(str, false);
            }
        }
    }
}
