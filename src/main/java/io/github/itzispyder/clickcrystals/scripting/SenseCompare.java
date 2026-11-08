package io.github.itzispyder.clickcrystals.scripting;

import java.util.Locale;
import java.util.Set;

/** Pure comparison functions for live-sensor predicates. */
public final class SenseCompare {
    private static final Set<String> UNARY = Set.of("empty", "nonempty", "true", "false");
    private SenseCompare() { }

    public static boolean isUnary(String operator) {
        return UNARY.contains(operator.toLowerCase(Locale.ROOT));
    }

    public static boolean check(String value, String operation, String expected) {
        if (value == null) return false; // unavailable game state must not pass any guard
        String op = operation.toLowerCase(Locale.ROOT);
        return switch (op) {
            case "eq", "==" -> value.equals(expected);
            case "ne", "!=" -> !value.equals(expected);
            case "contains" -> value.contains(expected);
            case "starts" -> value.startsWith(expected);
            case "ends" -> value.endsWith(expected);
            case "empty" -> value.isEmpty();
            case "nonempty" -> !value.isEmpty();
            case "true" -> "true".equalsIgnoreCase(value);
            case "false" -> "false".equalsIgnoreCase(value);
            case "gt", ">" -> compare(value, expected) > 0;
            case "ge", ">=" -> compare(value, expected) >= 0;
            case "lt", "<" -> compare(value, expected) < 0;
            case "le", "<=" -> compare(value, expected) <= 0;
            default -> throw new IllegalArgumentException("Invalid sensor operator: " + op);
        };
    }

    private static int compare(String left, String right) {
        double l = Double.parseDouble(left), r = Double.parseDouble(right);
        if (!Double.isFinite(l) || !Double.isFinite(r))
            throw new IllegalArgumentException("Non-finite sensor numeric comparison");
        return Double.compare(l, r);
    }
}
