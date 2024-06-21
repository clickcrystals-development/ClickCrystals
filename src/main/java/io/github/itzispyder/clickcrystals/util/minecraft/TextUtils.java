package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TextUtils implements Global {

    public static List<String> wordWrap(String input, int width) {
        return wordWrap(input, width, 1F);
    }

    public static List<String> charWrap(String input, int width) {
        return charWrap(input, width, 1F);
    }

    public static List<String> wordWrap(String input, int width, float textScale) {
        List<String> result = new ArrayList<>();
        var tr = mc.textRenderer;

        if (input == null || input.isEmpty() || tr == null || width <= 0) {
            return result;
        }

        for (String line : StringUtils.lines(input)) {
            StringBuilder current = new StringBuilder();

            for (String word : line.split(" ")) {
                int nextWidth = (int)(tr.getWidth(current + " " + word) * textScale);
                if (nextWidth == width) {
                    result.add(current + word);
                    current = new StringBuilder();
                }
                if (nextWidth > width) {
                    result.add(current.toString());
                    current = new StringBuilder();
                }
                if (nextWidth != width)
                    current.append(word).append(" ");
            }
            result.add(current.toString());
        }

        return result;
    }

    public static List<String> charWrap(String input, int width, float textScale) {
        List<String> result = new ArrayList<>();
        var tr = mc.textRenderer;

        if (input == null || input.isEmpty() || tr == null || width <= 0) {
            return result;
        }

        for (String line : StringUtils.lines(input)) {
            StringBuilder current = new StringBuilder();
            for (char c : line.toCharArray()) {
                if (tr.getWidth(current + String.valueOf(c)) * textScale > width) {
                    result.add(current.toString());
                    current = new StringBuilder();
                }
                current.append(c);
            }
            result.add(current.toString());
        }

        return result;
    }

    public static int getSelectionMax(String input, int toWidth) {
        var tr = mc.textRenderer;

        if (tr == null || input.isEmpty()) {
            return 0;
        }
        if (tr.getWidth(input) <= toWidth) {
            return input.length();
        }

        StringBuilder current = new StringBuilder();
        for (char c : input.toCharArray()) {
            current.append(c);
            if (tr.getWidth(current.toString()) >= toWidth) {
                return current.length();
            }
        }
        return 0;
    }
}