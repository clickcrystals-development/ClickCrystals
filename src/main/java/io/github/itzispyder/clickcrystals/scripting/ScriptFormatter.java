package io.github.itzispyder.clickcrystals.scripting;

/**
 * InDev, expect bugs and parse errors
 */
public class ScriptFormatter {

    /**
     * Compresses the ClickCrystals script inline a neat, single-line script by appending semicolons.
     * This feature is InDev, expect bugs and parse errors.
     * @param script script to compress
     * @return a single-line string representing the decompressed input script
     */
    public static String compress(String script) {
        StringBuilder result = new StringBuilder();
        for (String line : script.lines().toList()) {
            line = line.trim().replace("\\s+", " ");
            result.append(line);

            if (!line.isEmpty()) {
                if (!line.endsWith("{") && !line.endsWith("}") && !line.endsWith(";"))
                    result.append(';');
                result.append(' ');
            }
        }
        return result.toString();
    }

    /**
     * Decompresses the ClickCrystals script from a single-line string to a nicely formatted script by removing
     * semicolons and spacing out each line.
     * This feature is InDev, expect bugs and parse errors.
     * @param script script to decompress
     * @return a multi-line string representing the decompressed input script
     */
    public static String decompress(String script) {
        script = compress(script);
        StringBuilder result = new StringBuilder();

        for (String line : script.split(";")) {
            line = line.trim();
            result.append(line).append('\n');
        }

        char[] temp = result.toString().toCharArray();
        boolean spaced = false;
        result = new StringBuilder();

        for (char c : temp) {
            if (c == '{') {
                result.append(c).append('\n');
                spaced = true;
            }
            else if (c == '}') {
                result.append(c).append('\n');
                spaced = true;
            }
            else if (c == ' ') {
                if (!spaced) {
                    result.append(c);
                    spaced = true;
                }
            }
            else {
                result.append(c);
                spaced = false;
            }
        }

        int opens = 0;
        String resultTemp = result.toString();
        result = new StringBuilder();

        for (String line : resultTemp.lines().toList()) {
            if (line.endsWith("{"))
                opens++;
            else if (line.startsWith("}") || line.endsWith("}"))
                opens--;

            var bl = line.endsWith("{");
            result.append(bl && opens == 1 ? "\n" : "")
                    .append(" ".repeat((bl ? opens - 1 : opens) * 3))
                    .append(line)
                    .append('\n');
        }
        return result.toString();
    }
}
