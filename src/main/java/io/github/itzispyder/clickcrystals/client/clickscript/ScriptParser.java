package io.github.itzispyder.clickcrystals.client.clickscript;

import io.github.itzispyder.clickcrystals.client.clickscript.components.CommandLine;
import io.github.itzispyder.clickcrystals.client.clickscript.components.CommandStack;
import io.github.itzispyder.clickcrystals.util.misc.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ScriptParser {

    public static List<String> getAllSections(String line, char openingChar, char closingChar) {
        List<String> lines = new ArrayList<>();
        int index = 0;
        var section = firstSectionWithIndex(line, openingChar, closingChar);

        while (!section.left.isEmpty()) {
            lines.add(section.left);
            index += section.right;
            section = firstSectionWithIndex(line.substring(index), openingChar, closingChar);
        }

        return lines;
    }

    public static List<CommandLine> getAllLines(String line) {
        List<CommandLine> lines = new ArrayList<>();
        int index = 0;
        var section = firstSectionWithIndex(line, '{', '}');

        while (!section.left.isEmpty()) {
            lines.add(new CommandLine(section.left));
            index += section.right;
            section = firstSectionWithIndex(line.substring(index), '{', '}');
        }

        return lines;
    }

    public static CommandStack getStack(String line) {
        return new CommandStack(getAllLines(line));
    }

    public static String firstSection(String line, char enclosingChar) {
        return firstSection(line, enclosingChar, enclosingChar);
    }

    public static String firstSection(String line, char openingChar, char closingChar) {
        return firstSectionWithIndex(line, openingChar, closingChar).left;
    }

    public static CommandLine firstLine(String line) {
        return new CommandLine(firstSection(line, '{', '}'));
    }

    public static Pair<String, Integer> firstSectionWithIndex(String line, char openingChar, char closingChar) {
        line = line == null ? "" : line;
        StringBuilder result = new StringBuilder();

        if (line.isEmpty()) {
            return Pair.of(line, 0);
        }

        char[] chars = line.toCharArray();
        boolean began = false;
        int toIgnore = 0;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            boolean skip = i > 0 && chars[i - 1] == '\\';

            if (c == openingChar && !skip) { // start of the quote
                if (began) {
                    if (openingChar != closingChar) { // support deep operations
                        toIgnore++;
                    }
                }
                else {
                    began = true;
                    continue;
                }
            }

            if (c == closingChar && !skip && began) { // end of the quote
                if (toIgnore-- > 0) {
                    result.append(c);
                    continue;
                }
                return Pair.of(result.toString().trim(), i + 1);
            }
            else if (c == '\\' && !skip) {
                continue;
            }

            if (began) {
                result.append(c);
            }
        }

        String r = result.toString().trim();
        if (r.isEmpty()) {
            return Pair.of(r, chars.length);
        }

        String msg = "unclosed enclosing chars %s%s to mark string section".formatted(
                String.valueOf(openingChar),
                String.valueOf(closingChar)
        );
        throw new IllegalArgumentException(msg);
    }

    public static String readFile(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                result.append(line);

                if (!line.isEmpty()) {
                    result.append(" ");
                }
            }

            br.close();
            return result.toString().trim();
        }
        catch (Exception ex) {
            return "";
        }
    }

    public static String condenseLines(String string) {
        try {
            StringBuilder result = new StringBuilder();

            for (String line : string.lines().toArray(String[]::new)) {
                line = line.trim();
                result.append(line);

                if (!line.isEmpty()) {
                    result.append(" ");
                }
            }

            return result.toString().trim();
        }
        catch (Exception ex) {
            return "";
        }
    }
}
