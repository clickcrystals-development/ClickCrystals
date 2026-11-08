package io.github.itzispyder.clickcrystals.scripting;

/**
 * Removes CCS // comments before semicolon tokenization.
 * A comment marker is recognized at the beginning of a line or following
 * whitespace, and never inside a double-quoted string. This preserves
 * quoted URLs and unquoted http:// or https:// tokens.
 */
public final class ScriptComments {
    private ScriptComments() { }

    public static String stripLine(String source) {
        if (source == null || source.isEmpty()) return "";
        boolean inQuote = false;
        boolean escaped = false;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (escaped) {
                escaped = false;
                continue;
            }
            if (c == '\\') {
                escaped = true;
                continue;
            }
            if (c == '"') {
                inQuote = !inQuote;
                continue;
            }
            if (!inQuote && c == '/' && i + 1 < source.length() &&
                    source.charAt(i + 1) == '/' &&
                    (i == 0 || Character.isWhitespace(source.charAt(i - 1)))) {
                return source.substring(0, i).trim();
            }
        }
        return source.trim();
    }
}
