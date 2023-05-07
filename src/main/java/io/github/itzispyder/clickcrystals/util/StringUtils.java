package io.github.itzispyder.clickcrystals.util;

import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

    public static String capitalize(String s) {
        if (s.length() == 1) return s.toUpperCase();
        s = s.toLowerCase();
        return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
    }

    public static String capitalizeWords(String s) {
        s = s.replaceAll("[_-]"," ");
        String[] sArray = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String str : sArray) sb.append(capitalize(str)).append(" ");
        return sb.toString().trim();
    }

    public static char nextChar(String s, int index) {
        index ++;
        if (index >= s.length()) return ' ';
        return s.charAt(index);
    }

    public static List<String> wrapLines(String s, int maxLen) {
        return wrapLines(s, maxLen, false);
    }

    public static List<String> wrapLines(String s, int maxLen, boolean wordWrap) {
        final List<String> lines = new ArrayList<>();

        if (s.length() <= maxLen) {
            lines.add(s);
            return lines;
        }

        while (s.length() >= maxLen) {
            if (wordWrap) {
                int wrapAt = maxLen - 1;
                while (nextChar(s, wrapAt) != ' ') {
                    wrapAt ++;
                }
                lines.add(s.substring(0, wrapAt + 1).trim());
                s = s.substring(wrapAt + 1);
            }
            else {
                lines.add(s.substring(0, maxLen).trim());
                s = s.substring(maxLen);
            }
        }
        if (s.length() > 0) {
            lines.add(s.trim());
        }

        return lines;
    }
}
