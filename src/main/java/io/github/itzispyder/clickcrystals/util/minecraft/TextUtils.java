package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;

import java.util.ArrayList;
import java.util.List;

public class TextUtils implements Global {

    public static List<String> wrapLines(String input, int width) {
        List<String> result = new ArrayList<>();
        var tr = mc.textRenderer;

        if (input == null || input.isEmpty() || tr == null || width <= 0) {
            return result;
        }
        if (tr.getWidth(input) <= width) {
            result.add(input);
            return result;
        }

        for (String line : input.lines().toArray(String[]::new)) {
            StringBuilder current = new StringBuilder();
            for (char c : line.toCharArray()) {
                if (tr.getWidth(current + String.valueOf(c)) > width) {
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
