package io.github.itzispyder.clickcrystals.scripting;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/** Runtime variables and per-script rate limits. Cleared on script reload. */
public final class ScriptState {
    private static final Pattern KEY = Pattern.compile("[A-Za-z_][A-Za-z0-9_-]{0,63}");
    private static final int MAX_KEYS = 128;
    private final Map<String,String> values = new HashMap<>();
    private final Map<String,Long> cooldowns = new HashMap<>();

    private static void check(String key) {
        if (key == null || !KEY.matcher(key).matches())
            throw new IllegalArgumentException("Invalid state key");
    }
    public synchronized void set(String key, String value) {
        check(key);
        if (value == null || value.length() > 256) throw new IllegalArgumentException("State value too long");
        if (!values.containsKey(key) && values.size() >= MAX_KEYS) throw new IllegalStateException("Too many state keys");
        values.put(key,value);
    }
    public synchronized String get(String key) { check(key); return values.getOrDefault(key,""); }
    public synchronized void clear(String key) { check(key); values.remove(key); cooldowns.remove(key); }
    public synchronized void reset() { values.clear(); cooldowns.clear(); }
    public synchronized void add(String key, double amount) {
        check(key);
        if (!Double.isFinite(amount)) throw new IllegalArgumentException("Invalid amount");
        String prev = get(key);
        double n = (prev.isEmpty() ? 0 : Double.parseDouble(prev)) + amount;
        if (!Double.isFinite(n)) throw new IllegalArgumentException("Numeric overflow");
        set(key, n == Math.rint(n) && Math.abs(n) < 9007199254740992D ? Long.toString((long)n) : Double.toString(n));
    }
    public synchronized boolean matches(String key, String op, String other) {
        String val = get(key);
        return switch (op.toLowerCase(Locale.ROOT)) {
            case "eq", "==" -> val.equals(other);
            case "ne", "!=" -> !val.equals(other);
            case "gt", ">" -> compare(val,other) > 0;
            case "ge", ">=" -> compare(val,other) >= 0;
            case "lt", "<" -> compare(val,other) < 0;
            case "le", "<=" -> compare(val,other) <= 0;
            default -> throw new IllegalArgumentException("Invalid state operator");
        };
    }
    private static int compare(String x, String y) {
        double a = Double.parseDouble(x), b = Double.parseDouble(y);
        if (!Double.isFinite(a) || !Double.isFinite(b)) throw new IllegalArgumentException("Non-finite state comparison");
        return Double.compare(a,b);
    }
    public synchronized boolean acquire(String key,long cooldownMillis) {
        check(key);
        if (cooldownMillis < 0 || cooldownMillis > 3_600_000) throw new IllegalArgumentException("Cooldown out of bounds");
        if (!cooldowns.containsKey(key) && cooldowns.size() >= MAX_KEYS) throw new IllegalStateException("Too many gate keys");
        long now = System.nanoTime();
        Long until = cooldowns.get(key);
        if (until != null && now-until < 0) return false;
        cooldowns.put(key,now+cooldownMillis*1_000_000);
        return true;
    }
}
