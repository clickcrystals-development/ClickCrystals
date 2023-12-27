package io.github.itzispyder.clickcrystals.client.clickscript;

import io.github.itzispyder.clickcrystals.client.clickscript.components.CommandLine;
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

    public static String firstSection(String line, char enclosingChar) {
        return firstSection(line, enclosingChar, enclosingChar);
    }

    public static String firstSection(String line, char openingChar, char closingChar) {
        return firstSectionWithIndex(line, openingChar, closingChar).left;
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

    // script specific

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
                    if (!line.endsWith("{") && !line.endsWith("}") && !line.endsWith(";")) {
                        result.append(";");
                    }
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

    public static List<CommandLine> getStackLines(String line) {
        line = line == null ? "" : line;
        List<CommandLine> lines = new ArrayList<>();

        StringBuilder temp = new StringBuilder();
        char[] chars = line.toCharArray();
        boolean inQuote = false;
        int i = 0;

        while (i < chars.length) {
            char c = chars[i];
            boolean skip = i > 0 && chars[i - 1] == '\\';

            if (c == '"' && !skip) {
                inQuote = !inQuote;
            }

            if (c == '{' && !skip && !inQuote) {
                String subLine = line.substring(i);
                var section = firstSectionWithIndex(subLine, '{', '}');

                String lead = temp.toString().trim();
                lines.add(new CommandLine((lead.isEmpty() ? "%s%s" : "%s {%s}").formatted(lead, section.left)));

                temp = new StringBuilder();
                i += section.right;
                continue;
            }
            if (c == ';' && !skip && !inQuote) {
                String subLine = temp.toString().trim();

                if (!subLine.isEmpty()) {
                    lines.add(new CommandLine(subLine));
                }

                temp = new StringBuilder();
                i++;
                continue;
            }
            else {
                temp.append(c);
            }
            i++;
        }

        String remaining = temp.toString().trim();
        if (!remaining.isEmpty()) {
            lines.add(new CommandLine(remaining));
        }

        if (inQuote) {
            throw new IllegalArgumentException("unclosed quotation marks scanned in script");
        }
        return lines;
    }
}
