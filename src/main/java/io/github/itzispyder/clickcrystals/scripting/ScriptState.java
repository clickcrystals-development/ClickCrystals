package io.github.itzispyder.clickcrystals.scripting;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Isolated, bounded state for one ClickScript instance.
 * No Java reflection, file I/O or arbitrary code execution is exposed.
 */
public final class ScriptState {
    private static final Pattern KEY = Pattern.compile("[A-Za-z_][A-Za-z0-9_-]{0,63}");
    private static final int MAX_KEYS = 128;
    private static final int MAX_VALUE_LENGTH = 256;
    private final Map<String, String> values = new HashMap<>();
    private final Map<String, Long> cooldowns = new HashMap<>();

    private static void check(String key) {
        if (key == null || !KEY.matcher(key).matches())
            throw new IllegalArgumentException("Invalid state key: expected 1-64 letters, digits, _ or -");
    }

    public synchronized void set(String key, String value) {
        check(key);
        if (value == null || value.length() > MAX_VALUE_LENGTH)
            throw new IllegalArgumentException("State value is null or exceeds 256 characters");
        if (!values.containsKey(key) && values.size() >= MAX_KEYS)
            throw new IllegalStateException("Too many state variables (max 128)");
        values.put(key, value);
    }

    public synchronized String get(String key) {
        check(key);
        return values.getOrDefault(key, "");
    }

    public synchronized void clear(String key) {
        check(key);
        values.remove(key);
        cooldowns.remove(key);
    }

    public synchronized void reset() {
        values.clear();
        cooldowns.clear();
    }

    public synchronized void copy(String destination, String source) {
        set(destination, get(source));
    }

    public synchronized void append(String key, String suffix) {
        set(key, get(key) + suffix);
    }

    public synchronized void toggle(String key) {
        set(key, Boolean.toString(!Boolean.parseBoolean(get(key))));
    }

    public synchronized void length(String destination, String source) {
        set(destination, Integer.toString(get(source).length()));
    }

    private static double finite(double value) {
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Non-finite numeric state value");
        return value;
    }

    private static String format(double value) {
        finite(value);
        if (value == Math.rint(value) && Math.abs(value) < 9007199254740992D)
            return Long.toString((long) value);
        return Double.toString(value);
    }

    private double numericOrZero(String key) {
        String current = get(key);
        return current.isEmpty() ? 0.0D : finite(Double.parseDouble(current));
    }

    public synchronized void math(String key, String operator, double operand) {
        finite(operand);
        double current = numericOrZero(key);
        double result = switch (operator.toLowerCase(Locale.ROOT)) {
            case "add", "inc" -> current + operand;
            case "sub", "dec" -> current - operand;
            case "mul" -> current * operand;
            case "div" -> {
                if (operand == 0) throw new IllegalArgumentException("Division by zero");
                yield current / operand;
            }
            case "mod" -> {
                if (operand == 0) throw new IllegalArgumentException("Modulo by zero");
                yield current % operand;
            }
            case "min" -> Math.min(current, operand);
            case "max" -> Math.max(current, operand);
            default -> throw new IllegalArgumentException("Unsupported numeric state operator");
        };
        set(key, format(result));
    }

    /** Compatibility alias for v1.4.4 scripts. */
    public synchronized void add(String key, double delta) {
        math(key, "add", delta);
    }

    public synchronized void clamp(String key, double lower, double upper) {
        finite(lower);
        finite(upper);
        if (lower > upper) throw new IllegalArgumentException("Clamp lower bound exceeds upper bound");
        set(key, format(Math.max(lower, Math.min(upper, numericOrZero(key)))));
    }

    public synchronized boolean matches(String key, String operator, String other) {
        String value = get(key);
        return switch (operator.toLowerCase(Locale.ROOT)) {
            case "eq", "==" -> value.equals(other);
            case "ne", "!=" -> !value.equals(other);
            case "contains" -> value.contains(other);
            case "starts", "starts_with" -> value.startsWith(other);
            case "ends", "ends_with" -> value.endsWith(other);
            case "empty" -> value.isEmpty();
            case "nonempty", "not_empty" -> !value.isEmpty();
            case "true" -> Boolean.parseBoolean(value);
            case "false" -> !Boolean.parseBoolean(value);
            case "gt", ">" -> compare(value, other) > 0;
            case "ge", ">=" -> compare(value, other) >= 0;
            case "lt", "<" -> compare(value, other) < 0;
            case "le", "<=" -> compare(value, other) <= 0;
            default -> throw new IllegalArgumentException("Invalid state comparison: " + operator);
        };
    }

    private static int compare(String a, String b) {
        return Double.compare(finite(Double.parseDouble(a)), finite(Double.parseDouble(b)));
    }

    /**
     * Returns true only when the specified per-script gate is available.
     * Uses monotonic time to avoid wall-clock adjustments.
     */
    public synchronized boolean acquire(String key, long cooldownMillis) {
        check(key);
        if (cooldownMillis < 0 || cooldownMillis > 3_600_000)
            throw new IllegalArgumentException("Cooldown must be between 0 and 3600000 ms");
        if (!cooldowns.containsKey(key) && cooldowns.size() >= MAX_KEYS)
            throw new IllegalStateException("Too many gate keys (max 128)");
        long now = System.nanoTime();
        Long deadline = cooldowns.get(key);
        if (deadline != null && now - deadline < 0) return false;
        cooldowns.put(key, now + cooldownMillis * 1_000_000L);
        return true;
    }
}
